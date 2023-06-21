package nl.han.oose.dea.datasource;

import nl.han.oose.dea.controllers.dto.PlaylistDTO;
import nl.han.oose.dea.controllers.dto.TrackDTO;
import nl.han.oose.dea.datasource.dao.PlaylistDAO;
import nl.han.oose.dea.datasource.databaseConnection.ConnectionManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PlaylistDAOTest {
    private final static String TOKEN = "token";
    private final static int PLAYLIST_ID = 1;

    private PlaylistDAO sut;
    private ConnectionManager connectionManager;
    private Connection mockedConnection;
    private PreparedStatement preparedStatement;
    private ResultSet mockedResultSet;
    private PlaylistDTO playlist;
    private ArrayList<PlaylistDTO> playlists;

    @BeforeEach
    public void setup() throws SQLException {
        sut = new PlaylistDAO();
        connectionManager = mock(ConnectionManager.class);
        mockedResultSet = mock(ResultSet.class);
        mockedConnection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        sut.setConnectionManager(connectionManager);

        playlist = new PlaylistDTO();
        playlists = new ArrayList<PlaylistDTO>();

        when(connectionManager.getConnection()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(mockedResultSet);
    }

    @Test
    void getAllPlaylistsQueryTest() throws Exception {
        sut.getAllPlaylists(TOKEN);
        verify(preparedStatement).executeQuery();
    }

    @Test
    void queryPlaylistsQueryTest() throws Exception {
        sut.queryPlaylists(TOKEN);
        verify(preparedStatement).executeQuery();
    }

    @Test
    void editPlaylistNameQueryTest() throws Exception {
        sut.editPlaylistName(playlist, PLAYLIST_ID);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void addPlaylistQueryTest() throws Exception {
        sut.addPlaylist(TOKEN, new PlaylistDTO());
        verify(preparedStatement.executeQuery());
    }

    @Test
    void deletePlaylistQueryTest() {
        try {
            doThrow(Exception.class).when(connectionManager).startConnection();

            Assertions.assertThrows(Exception.class, () -> sut.deletePlaylist(PLAYLIST_ID));
        } catch(Exception e) {
        }
    }

    @Test
    void calculateLengthQueryTest() {
        try {
            doThrow(Exception.class).when(connectionManager).startConnection();

            Assertions.assertThrows(Exception.class, () -> sut.calculateLength(playlists));
        } catch(Exception e) {
        }
    }
}
