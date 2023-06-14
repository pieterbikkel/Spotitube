package nl.han.oose.dea.controllers.dto.response;

import nl.han.oose.dea.controllers.dto.PlaylistDTO;

import java.util.ArrayList;

public class PlaylistsResponseDTO {
    private ArrayList<PlaylistDTO> playlists;
    private int length;

    public PlaylistsResponseDTO() {
    }

    public PlaylistsResponseDTO(ArrayList<PlaylistDTO> playlists, int length) {
        this.playlists = playlists;
        this.length = length;
    }

    public ArrayList<PlaylistDTO> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(ArrayList<PlaylistDTO> playlists) {
        this.playlists = playlists;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
