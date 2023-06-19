package nl.han.oose.dea.datasource.dao;

import jakarta.inject.Inject;
import nl.han.oose.dea.controllers.dto.response.TrackResponseDTO;
import nl.han.oose.dea.controllers.dto.response.TracksResponseDTO;
import nl.han.oose.dea.datasource.databaseConnection.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TrackDAO {

    @Inject
    private ConnectionManager connectionManager;
    public TracksResponseDTO getAllTracksFromPlaylist(int playlistId) throws SQLException {
        connectionManager.startConnection();
        var sql = "SELECT * FROM track WHERE trackId IN (SELECT trackID FROM PlaylistTrack WHERE playlistId = ?)";
        var preparedStatement = connectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, playlistId);
        TracksResponseDTO tracks = mapToTracks(preparedStatement.executeQuery());
        connectionManager.stopConnection();
        return tracks;
    }

    public TracksResponseDTO getAllTracksNotInPlaylist(int playlistId) throws SQLException {
        connectionManager.startConnection();
        var sql = "SELECT * FROM track WHERE trackId NOT IN (SELECT trackID FROM PlaylistTrack WHERE playlistId = ?);";
        var preparedStatement = connectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, playlistId);
        var tracks = mapToTracks(preparedStatement.executeQuery());
        connectionManager.stopConnection();
        return tracks;
    }

    private TracksResponseDTO mapToTracks(ResultSet rs) throws SQLException {
        var tracks = new ArrayList<TrackResponseDTO>();

        while (rs.next()) {
            int id = rs.getInt("trackId");
            String title = rs.getString("titel");
            String performer = rs.getString("performer");
            int duration = rs.getInt("afspeelduur");
            String album = rs.getString("album");
            int playCount = rs.getInt("aantalKeerAfgespeeld");
            String publicationDate = rs.getString("publicatieDatum");
            String description = rs.getString("beschrijving");
            boolean offlineAvailable = rs.getBoolean("offlineAvailable");

            TrackResponseDTO track = new TrackResponseDTO(id, duration, playCount, title, performer, album, publicationDate, description, offlineAvailable);

            tracks.add(track);
        }
        return new TracksResponseDTO(tracks);
    }

    public void deleteTrackFromPlaylist(int playlistId, int trackId) throws SQLException {
        connectionManager.startConnection();
        var sql = "DELETE FROM PlaylistTrack WHERE playlistId = ? AND trackId = ?;";
        var stmt = connectionManager.getConnection().prepareStatement(sql);
        stmt.setInt(1, playlistId);
        stmt.setInt(2, trackId);
        stmt.executeUpdate();
        connectionManager.stopConnection();
    }

    public void addTrackToPlaylist(int playlistId, TrackResponseDTO track) throws SQLException {
        connectionManager.startConnection();
        var sql = "INSERT INTO PlaylistTrack (playlistId, trackId) VALUES (?, ?);";
        var preparedStatement = connectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, playlistId);
        preparedStatement.setInt(2, track.getId());
        preparedStatement.executeUpdate();
        connectionManager.stopConnection();
    }
}
