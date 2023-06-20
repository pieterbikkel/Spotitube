package nl.han.oose.dea.controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.controllers.dto.PlaylistDTO;
import nl.han.oose.dea.controllers.dto.TrackDTO;
import nl.han.oose.dea.datasource.dao.LoginDAO;
import nl.han.oose.dea.datasource.dao.PlaylistDAO;
import nl.han.oose.dea.datasource.dao.TrackDAO;
import nl.han.oose.dea.datasource.exceptions.ForbiddenResourceException;
import nl.han.oose.dea.services.LoginService;

@Path("playlists")
public class PlaylistController {

    @Inject
    private PlaylistDAO playlistDAO;

    @Inject
    private LoginService loginService;

    @Inject
    private TrackDAO trackDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPlaylists(@QueryParam("token") String token) throws ForbiddenResourceException {
        this.loginService.verifyToken(token);
        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(playlistDAO.getAllPlaylists(token))
                    .build();
        } catch (Exception e) {
            return Response.status(400).build();
        }
    }

    @GET
    @Path("/{playlist}/tracks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracksFromPlaylist(@PathParam("playlist") int playlistId, @QueryParam("token") String token) throws ForbiddenResourceException {
        this.loginService.verifyToken(token);
        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(trackDAO.getAllTracksFromPlaylist(playlistId))
                    .build();
        } catch (Exception e) {
            return Response.status(400).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPlaylist(PlaylistDTO playlist, @QueryParam("token") String token) throws ForbiddenResourceException {
        this.loginService.verifyToken(token);
        try {
            playlistDAO.addPlaylist(token, playlist);
            return Response
                    .status(Response.Status.CREATED)
                    .entity(playlistDAO.getAllPlaylists(token))
                    .build();
        } catch (Exception e) {
            return Response.status(400).build();
        }
    }

    @POST
    @Path("/{id}/tracks")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTrackToPlaylist(@QueryParam("token") String token, @PathParam("id") int playlistId, TrackDTO track) throws ForbiddenResourceException {
        this.loginService.verifyToken(token);
        try {
            trackDAO.addTrackToPlaylist(playlistId, track);
            return Response
                    .status(Response.Status.CREATED)
                    .entity(trackDAO.getAllTracksFromPlaylist(playlistId))
                    .build();
        } catch (Exception e) {
            return Response.status(400).build();
        }
    }

    @DELETE
    @Path("/{playlist}/tracks/{track}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTrackFromPlaylist(@QueryParam("token") String token, @PathParam("playlist") int playlistId, @PathParam("track") int trackId) throws ForbiddenResourceException {
        this.loginService.verifyToken(token);
        try {
            trackDAO.deleteTrackFromPlaylist(playlistId, trackId);
            return Response
                    .status(Response.Status.ACCEPTED)
                    .entity(trackDAO.getAllTracksFromPlaylist(playlistId))
                    .build();
        } catch (Exception e) {
            return Response.status(400).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePlaylist(@PathParam("id") int id, @QueryParam("token") String token) throws ForbiddenResourceException {
        this.loginService.verifyToken(token);
        try {
            playlistDAO.deletePlaylist(id);
            return Response
                    .status(Response.Status.ACCEPTED)
                    .entity(playlistDAO.getAllPlaylists(token))
                    .build();
        } catch (Exception e) {
            return Response.status(400).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editPlaylist(PlaylistDTO playlist, @PathParam("id") int id, @QueryParam("token") String token) throws ForbiddenResourceException {
        this.loginService.verifyToken(token);
        try {
            playlistDAO.editPlaylistName(playlist, id);
            return Response
                    .status(Response.Status.ACCEPTED)
                    .entity(playlistDAO.getAllPlaylists(token))
                    .build();
        } catch (Exception e) {
            return Response.status(400).build();
        }
    }

    @Inject
    public void setPlaylistDAO(PlaylistDAO playlistDAO) {
        this.playlistDAO = playlistDAO;
    }

    @Inject
    public void setTrackDAO(TrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }

    @Inject
    public void setLoginService(LoginService loginService) { this.loginService = loginService; };
}
