package nl.han.oose.dea.controllers;

import com.mysql.cj.log.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.datasource.dao.TrackDAO;
import nl.han.oose.dea.datasource.exceptions.ForbiddenResourceException;
import nl.han.oose.dea.services.LoginService;

import java.sql.SQLException;

@Path("/tracks")
public class TrackController {

    @Inject
    private TrackDAO trackDAO;

    @Inject
    private LoginService loginService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracksNotInPlaylist(@QueryParam("token") String token, @QueryParam("forPlaylist") int playlistId) throws ForbiddenResourceException {
        loginService.verifyToken(token);
        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(trackDAO.getAllTracksNotInPlaylist(playlistId))
                    .build();
        } catch(SQLException e) {
            return Response.status(400).build();
        }
    }

    @Inject
    void setTrackDAO(TrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }

    @Inject
    void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }
}
