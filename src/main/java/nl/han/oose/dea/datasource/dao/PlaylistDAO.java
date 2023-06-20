package nl.han.oose.dea.datasource.dao;

import jakarta.inject.Inject;
import nl.han.oose.dea.controllers.dto.PlaylistDTO;
import nl.han.oose.dea.controllers.dto.response.PlaylistsResponseDTO;
import nl.han.oose.dea.datasource.databaseConnection.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class PlaylistDAO {
    @Inject
    private ConnectionManager connectionManager;

    public PlaylistsResponseDTO getAllPlaylists(String token) throws SQLException {
        connectionManager.startConnection();
        ArrayList<PlaylistDTO> playlists = queryPlaylists(token);
        int playlistLength = calculateLength(playlists);
        connectionManager.stopConnection();
        return new PlaylistsResponseDTO(playlists, playlistLength);
    }

    public void editPlaylistName(PlaylistDTO playlist, int id) throws SQLException {
        connectionManager.startConnection();
        var sql = "UPDATE playlist SET naam = ? WHERE playlistId = ?;";
        var preparedStatement = connectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, playlist.getName());
        preparedStatement.setInt(2, id);
        preparedStatement.executeUpdate();
        connectionManager.stopConnection();
    }

    public void addPlaylist(String token, PlaylistDTO playlistDTO) throws SQLException {
        connectionManager.startConnection();
        var sql = "INSERT INTO playlist (naam, eigenaar) VALUES (?, (SELECT eigenaarId FROM eigenaar WHERE token = ?))";
        var preparedStatement = connectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, playlistDTO.getName());
        preparedStatement.setString(2, token);
        preparedStatement.executeUpdate();
        connectionManager.stopConnection();
    }

    public void deletePlaylist(int id) throws SQLException {
        connectionManager.startConnection();
        var sql = "DELETE FROM playlist WHERE playlistId = ?;";
        var preparedStatement = connectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        connectionManager.stopConnection();
    }

    public ArrayList<PlaylistDTO> queryPlaylists(String token) throws SQLException {
        var playlists = new ArrayList<PlaylistDTO>();
        var sql = "SELECT * FROM playlist p JOIN eigenaar e ON p.eigenaar = e.eigenaarId WHERE e.token = ?;";
        var preparedStatement = connectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, token);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("playlistId");
            String name = resultSet.getString("naam");
            String resultToken = resultSet.getString("token");
            Boolean isOwner = Objects.equals(token, resultToken);
            PlaylistDTO playlist = new PlaylistDTO(id, name, isOwner);
            playlists.add(playlist);
        }

        return playlists;
    }

    public int calculateLength(ArrayList<PlaylistDTO> playlists) throws SQLException {
        int duration = 0;

        for (PlaylistDTO playlist : playlists) {

            connectionManager.startConnection();
            PreparedStatement statement = connectionManager.getConnection().prepareStatement("SELECT SUM(t.afspeelduur) AS `afspeelduur` FROM track t INNER JOIN PlaylistTrack PT ON PT.trackId = t.trackId WHERE PT.playlistId = ?");
            statement.setInt(1, playlist.getId());
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                duration += result.getInt("afspeelduur");
            }
        }

        return duration;
    }

    @Inject
    public void setConnectionManager(ConnectionManager connectionManager)
    { this.connectionManager = connectionManager; }

}
