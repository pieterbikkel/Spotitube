package nl.han.oose.dea.datasource;

import nl.han.oose.dea.controllers.dto.PlaylistDTO;
import nl.han.oose.dea.controllers.dto.TrackDTO;
import nl.han.oose.dea.controllers.dto.response.TracksResponseDTO;
import nl.han.oose.dea.datasource.dao.PlaylistDAO;
import nl.han.oose.dea.datasource.dao.TrackDAO;
import nl.han.oose.dea.datasource.databaseConnection.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TrackDAOTest {
    private final static String TOKEN = "token";
    private final static int PLAYLIST_ID = 1;
    private final static int TRACK_ID = 1;

    private TrackDAO sut;
    private ConnectionManager connectionManager;
    private Connection mockedConnection;
    private PreparedStatement preparedStatement;
    private ResultSet mockedResultSet;
    private PlaylistDTO playlist;
    private ArrayList<PlaylistDTO> playlists;
    private TrackDTO track;

    @BeforeEach
    public void setup() throws SQLException {
        sut = new TrackDAO();
        connectionManager = mock(ConnectionManager.class);
        mockedResultSet = mock(ResultSet.class);
        mockedConnection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        sut.setConnectionManager(connectionManager);

        playlist = new PlaylistDTO();
        track = new TrackDTO();

        when(connectionManager.getConnection()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(mockedResultSet);
    }

    @Test
    void getAllTracksFromPlaylistQueryTest() throws Exception {
        sut.getAllTracksFromPlaylist(PLAYLIST_ID);
        verify(preparedStatement).executeQuery();
    }

    @Test
    void getAllTracksNotInPlaylistQueryTest() throws Exception {
        sut.getAllTracksNotInPlaylist(PLAYLIST_ID);
        verify(preparedStatement).executeQuery();
    }

    @Test
    void deleteTrackFromPlaylistQueryTest() throws Exception {
        sut.deleteTrackFromPlaylist(PLAYLIST_ID, TRACK_ID);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void addTrackToPlaylistQueryTest() throws Exception {
        sut.addTrackToPlaylist(PLAYLIST_ID, track);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testMapToTracks() throws SQLException {
        var date = new  Date().toString();

        when(mockedResultSet.next()).thenReturn(true, true, false);
        when(mockedResultSet.getInt("trackId")).thenReturn(1, 2);
        when(mockedResultSet.getString("titel")).thenReturn("Track 1", "Track 2");
        when(mockedResultSet.getString("performer")).thenReturn("Performer 1", "Performer 2");
        when(mockedResultSet.getInt("afspeelduur")).thenReturn(180, 240);
        when(mockedResultSet.getString("album")).thenReturn("album 1", "album 2");
        when(mockedResultSet.getInt("aantalKeerAfgespeeld")).thenReturn(1, 2);
        when(mockedResultSet.getString("publicatieDatum")).thenReturn(date, date);
        when(mockedResultSet.getString("beschrijving")).thenReturn("desc1", "desc2");
        when(mockedResultSet.getBoolean("offlineAvailable")).thenReturn(false, true);

        // Step 3: Invoke the method being tested

        TracksResponseDTO response = sut.mapToTracks(mockedResultSet);

        ArrayList<TrackDTO> tracks = response.getTracks();
        assertEquals(2, tracks.size());

        TrackDTO track1 = tracks.get(0);
        assertEquals(1, track1.getId());
        assertEquals("Track 1", track1.getTitle());
        assertEquals("Performer 1", track1.getPerformer());
        assertEquals(180, track1.getDuration());
        assertEquals("album 1", track1.getAlbum());
        assertEquals(1, track1.getPlaycount());
        assertEquals(date, track1.getPublicationDate());
        assertEquals("desc1", track1.getDescription());
        assertEquals(false, track1.isOfflineAvailable());

        TrackDTO track2 = tracks.get(1);
        assertEquals(2, track2.getId());
        assertEquals("Track 2", track2.getTitle());
        assertEquals("Performer 2", track2.getPerformer());
        assertEquals(240, track2.getDuration());
        assertEquals("album 2", track2.getAlbum());
        assertEquals(2, track2.getPlaycount());
        assertEquals(date, track2.getPublicationDate());
        assertEquals("desc2", track2.getDescription());
        assertEquals(true, track2.isOfflineAvailable());
    }

}
