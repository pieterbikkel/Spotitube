package nl.han.oose.dea.controllers;

import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.datasource.dao.TrackDAO;
import nl.han.oose.dea.services.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

public class TrackControllerTest {

    private TrackController sut;
    private TrackDAO trackDAO;
    private LoginService loginService;

    private final static String TOKEN = "token";
    private final static int PLAYLIST_ID = 1;

    @BeforeEach
    void setup() {
        sut = new TrackController();

        trackDAO = mock(TrackDAO.class);
        loginService = mock(LoginService.class);

        sut.setTrackDAO(trackDAO);
        sut.setLoginService(loginService);
    }

    @Test
    void getTracksNotInPlaylistSuccess() {
        try {
            assertEquals(Response.Status.OK.getStatusCode(), sut.getTracksNotInPlaylist(TOKEN, PLAYLIST_ID).getStatus());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void getTracksNotInPlaylistBadRequest() {
        try {
            doThrow(SQLException.class).when(trackDAO).getAllTracksNotInPlaylist(PLAYLIST_ID);
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), sut.getTracksNotInPlaylist(TOKEN, PLAYLIST_ID).getStatus());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void getTracksNotInPlaylistForbiddenResource() {
        try {
            doThrow(ForbiddenException.class).when(loginService).verifyToken(TOKEN);
            assertThrows(ForbiddenException.class, () -> sut.getTracksNotInPlaylist(TOKEN, PLAYLIST_ID));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
