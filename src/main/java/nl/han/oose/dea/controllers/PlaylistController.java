package nl.han.oose.dea.controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.controllers.dto.PlaylistDTO;
import nl.han.oose.dea.controllers.dto.response.TrackResponseDTO;
import nl.han.oose.dea.datasource.dao.PlaylistDAO;
import nl.han.oose.dea.datasource.dao.TrackDAO;
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
    public Response getAllPlaylists(@QueryParam("token") String token) throws Exception {
        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(playlistDAO.getAllPlaylists(token))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GET
    @Path("/{playlist}/tracks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracksFromPlaylist(@PathParam("playlist") int playlistId, @QueryParam("token") String token) throws Exception {
        try {
            this.loginService.verifyToken(token);
            return Response
                    .status(Response.Status.OK)
                    .entity(trackDAO.getAllTracksFromPlaylist(playlistId))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPlaylist(PlaylistDTO playlist, @QueryParam("token") String token) throws Exception {
        try {
            playlistDAO.addPlaylist(token, playlist);
            return Response
                    .status(Response.Status.OK)
                    .entity(playlistDAO.getAllPlaylists(token))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @POST
    @Path("/{id}/tracks")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTrackToPlaylist(@QueryParam("token") String token, @PathParam("id") int playlistId, TrackResponseDTO track) throws Exception {
        try {
            this.loginService.verifyToken(token);
            trackDAO.addTrackToPlaylist(playlistId, track);
            return Response
                    .status(Response.Status.OK)
                    .entity(trackDAO.getAllTracksFromPlaylist(playlistId))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @DELETE
    @Path("/{playlist}/tracks/{track}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTrackFromPlaylist(@QueryParam("token") String token, @PathParam("playlist") int playlistId, @PathParam("track") int trackId) throws Exception {
        try {
            this.loginService.verifyToken(token);
            trackDAO.deleteTrackFromPlaylist(playlistId, trackId);
            return Response
                    .status(Response.Status.OK)
                    .entity(trackDAO.getAllTracksFromPlaylist(playlistId))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePlaylist(@PathParam("id") int id, @QueryParam("token") String token) throws Exception {
        try {
            this.loginService.verifyToken(token);
            playlistDAO.deletePlaylist(id);
            return Response
                    .status(Response.Status.OK)
                    .entity(playlistDAO.getAllPlaylists(token))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editPlaylist(PlaylistDTO playlist, @PathParam("id") int id, @QueryParam("token") String token) throws Exception {
        try {
            this.loginService.verifyToken(token);
            playlistDAO.editPlaylistName(playlist, id);
            return Response
                    .status(Response.Status.OK)
                    .entity(playlistDAO.getAllPlaylists(token))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
