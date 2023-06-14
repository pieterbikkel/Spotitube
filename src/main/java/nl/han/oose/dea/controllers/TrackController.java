package nl.han.oose.dea.controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.datasource.dao.TrackDAO;

import java.sql.SQLException;

@Path("/tracks")
public class TrackController {

    @Inject
    private TrackDAO trackDAO;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracksNotInPlaylist(@QueryParam("token") String token, @QueryParam("forPlaylist") int playlistId) {
        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(trackDAO.getAllTracksNotInPlaylist(playlistId))
                    .build();
        } catch(SQLException e) {
            e.printStackTrace();
            throw new BadRequestException();
        }
    }
}
