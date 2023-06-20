package nl.han.oose.dea.controllers;

import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.controllers.dto.PlaylistDTO;
import nl.han.oose.dea.controllers.dto.TrackDTO;
import nl.han.oose.dea.datasource.dao.PlaylistDAO;
import nl.han.oose.dea.datasource.dao.TrackDAO;
import nl.han.oose.dea.services.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PlaylistControllerTest {

    private PlaylistController sut;
    private PlaylistDAO playlistDAO;
    private TrackDAO trackDAO;
    private PlaylistDTO playlistDTO;
    private TrackDTO trackDTO;
    private LoginService loginService;

    private static final String TOKEN = "token";
    private static final int PLAYLIST_ID = 1;
    private static final int TRACK_ID = 2;

    @BeforeEach
    void setup() {
        sut = new PlaylistController();

        playlistDAO = mock(PlaylistDAO.class);
        trackDAO = mock(TrackDAO.class);
        loginService = mock(LoginService.class);

        sut.setPlaylistDAO(playlistDAO);
        sut.setTrackDAO(trackDAO);
        sut.setLoginService(loginService);

        playlistDTO = mock(PlaylistDTO.class);
        trackDTO = mock(TrackDTO.class);
    }

    @Test
    void getAllPlaylistsSuccess() {
        try {
            doNothing().when(loginService).verifyToken(TOKEN);
            assertEquals(Response.Status.OK.getStatusCode(), sut.getAllPlaylists(TOKEN).getStatus());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void getAllPlaylistsBadRequest() {
        try {
            when(playlistDAO.getAllPlaylists(anyString())).thenAnswer((t) -> { throw new SQLException(); });

            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), sut.getAllPlaylists(TOKEN).getStatus());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void getAllPlaylistsForbiddenResource() {
        try {
            doThrow(ForbiddenException.class).when(loginService).verifyToken(TOKEN);
            assertThrows(ForbiddenException.class, () -> sut.getAllPlaylists(TOKEN));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void getTracksFromPlaylistSuccess() {
        try {
            doNothing().when(loginService).verifyToken(TOKEN);
            assertEquals(Response.Status.OK.getStatusCode(), sut.getTracksFromPlaylist(PLAYLIST_ID, TOKEN).getStatus());
        } catch (Exception e) {
            fail();
        }
    }
    @Test
    void getTracksFromPlaylistBadRequest() {
        try {
            when(trackDAO.getAllTracksFromPlaylist(PLAYLIST_ID)).thenAnswer((t) -> { throw new SQLException(); });

            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), sut.getTracksFromPlaylist(PLAYLIST_ID, TOKEN).getStatus());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void getTracksFromPlaylistForbiddenResource() {
        try {
            doThrow(ForbiddenException.class).when(loginService).verifyToken(TOKEN);
            assertThrows(ForbiddenException.class, () -> sut.getTracksFromPlaylist(PLAYLIST_ID, TOKEN));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void addPlaylistSuccess() {
        try {
            assertEquals(Response.Status.CREATED.getStatusCode(), sut.addPlaylist(playlistDTO, TOKEN).getStatus());
        } catch (Exception e) {
            fail();
        }

    }

    @Test
    void addPlaylistBadRequest() {
        try {
            doThrow(SQLException.class).when(playlistDAO).addPlaylist(TOKEN, playlistDTO);
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), sut.addPlaylist(playlistDTO, TOKEN).getStatus());
        } catch (Exception e) {
            fail();
        }
    }
    @Test
    void addPlaylistForbiddenResource() {
        try {
            doThrow(ForbiddenException.class).when(playlistDAO).addPlaylist(TOKEN, playlistDTO);

            assertThrows(ForbiddenException.class, () -> playlistDAO.addPlaylist(TOKEN, playlistDTO));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void addTrackToPlaylistSuccess() {
        try {
            assertEquals(Response.Status.CREATED.getStatusCode(), sut.addTrackToPlaylist(TOKEN, PLAYLIST_ID, trackDTO).getStatus());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void addTrackToPlaylistBadRequest() {
        try {
            doThrow(SQLException.class).when(trackDAO).addTrackToPlaylist(1, trackDTO);
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), sut.addTrackToPlaylist(TOKEN, PLAYLIST_ID, trackDTO).getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void addTrackToPlaylistForbiddenResource() {
        try {
            doThrow(ForbiddenException.class).when(trackDAO).addTrackToPlaylist(PLAYLIST_ID, trackDTO);
            assertThrows(ForbiddenException.class, () -> trackDAO.addTrackToPlaylist(PLAYLIST_ID, trackDTO));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void deleteTrackFromPlaylistSuccess() {
        assertEquals(Response.Status.ACCEPTED.getStatusCode(), sut.deleteTrackFromPlaylist(TOKEN, PLAYLIST_ID, TRACK_ID).getStatus());
    }

    @Test
    void deleteTrackFromPlaylistBadRequest() {
        try {
            doThrow(SQLException.class).when(trackDAO).deleteTrackFromPlaylist(PLAYLIST_ID, TRACK_ID);
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), sut.deleteTrackFromPlaylist(TOKEN, PLAYLIST_ID, TRACK_ID).getStatus());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void deleteTrackFromPlaylistForbiddenResource() {
        try {
            doThrow(ForbiddenException.class).when(loginService).verifyToken(TOKEN);
            assertThrows(ForbiddenException.class, () -> sut.deleteTrackFromPlaylist(TOKEN, PLAYLIST_ID, TRACK_ID));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void deletePlaylistSuccess() {
        assertEquals(Response.Status.ACCEPTED.getStatusCode(), sut.deletePlaylist(PLAYLIST_ID, TOKEN).getStatus());
    }

    @Test
    void deletePlaylistBadRequest() {
        try {
            doThrow(SQLException.class).when(playlistDAO).deletePlaylist(PLAYLIST_ID);
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), sut.deletePlaylist(PLAYLIST_ID, TOKEN).getStatus());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void deletePlaylistForbiddenResource() {
        try {
            doThrow(ForbiddenException.class).when(loginService).verifyToken(TOKEN);
            assertThrows(ForbiddenException.class, () -> sut.deletePlaylist(PLAYLIST_ID, TOKEN));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void editPlaylistSuccess() {
        assertEquals(Response.Status.ACCEPTED.getStatusCode(), sut.deletePlaylist(PLAYLIST_ID, TOKEN).getStatus());
    }

    @Test
    void editPlaylistBadRequest() {
        try {
            doThrow(SQLException.class).when(playlistDAO).editPlaylistName(playlistDTO, PLAYLIST_ID);
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), sut.editPlaylist(playlistDTO, PLAYLIST_ID, TOKEN).getStatus());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void editPlaylistForbiddenResource() {
        try {
            doThrow(ForbiddenException.class).when(loginService).verifyToken(TOKEN);
            assertThrows(ForbiddenException.class, () -> sut.editPlaylist(playlistDTO, PLAYLIST_ID, TOKEN));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
