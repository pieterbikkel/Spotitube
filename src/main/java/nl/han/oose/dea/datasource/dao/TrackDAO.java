package nl.han.oose.dea.datasource.dao;

import jakarta.inject.Inject;
import nl.han.oose.dea.controllers.dto.TrackDTO;
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
        var sql = "SELECT * FROM track WHERE track_id IN (SELECT track_id FROM PlaylistTrack WHERE playlist_id = ?)";
        var preparedStatement = connectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, playlistId);
        TracksResponseDTO tracks = mapToTracks(preparedStatement.executeQuery());
        connectionManager.stopConnection();
        return tracks;
    }

    public TracksResponseDTO getAllTracksNotInPlaylist(int playlistId) throws SQLException {
        connectionManager.startConnection();
        var sql = "SELECT * FROM track WHERE track_id NOT IN (SELECT track_id FROM PlaylistTrack WHERE playlist_id = ?);";
        var preparedStatement = connectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, playlistId);
        var tracks = mapToTracks(preparedStatement.executeQuery());
        connectionManager.stopConnection();
        return tracks;
    }

    public TracksResponseDTO mapToTracks(ResultSet rs) throws SQLException {
        var tracks = new ArrayList<TrackDTO>();

        while (rs.next()) {
            int id = rs.getInt("track_id");
            String title = rs.getString("title");
            String performer = rs.getString("performer");
            int duration = rs.getInt("duration");
            String album = rs.getString("album");
            int playCount = rs.getInt("play_count");
            String publicationDate = rs.getString("publication_date");
            String description = rs.getString("track_description");
            boolean offlineAvailable = rs.getBoolean("offline_available");

            TrackDTO track = new TrackDTO(id, duration, playCount, title, performer, album, publicationDate, description, offlineAvailable);

            tracks.add(track);
        }
        return new TracksResponseDTO(tracks);
    }

    public void deleteTrackFromPlaylist(int playlistId, int trackId) throws SQLException {
        connectionManager.startConnection();
        var sql = "DELETE FROM PlaylistTrack WHERE playlist_id = ? AND track_id = ?;";
        var stmt = connectionManager.getConnection().prepareStatement(sql);
        stmt.setInt(1, playlistId);
        stmt.setInt(2, trackId);
        stmt.executeUpdate();
        connectionManager.stopConnection();
    }

    public void addTrackToPlaylist(int playlistId, TrackDTO track) throws SQLException {
        connectionManager.startConnection();
        var sql = "INSERT INTO PlaylistTrack (playlist_id, track_id) VALUES (?, ?);";
        var preparedStatement = connectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, playlistId);
        preparedStatement.setInt(2, track.getId());
        preparedStatement.executeUpdate();
        connectionManager.stopConnection();
    }

    @Inject
    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
}
