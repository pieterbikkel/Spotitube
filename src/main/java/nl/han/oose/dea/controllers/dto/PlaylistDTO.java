package nl.han.oose.dea.controllers.dto;

import nl.han.oose.dea.controllers.dto.response.TrackResponseDTO;

import java.util.ArrayList;

public class PlaylistDTO {
    private int id;
    private String name;
    private boolean owner;
    private ArrayList<TrackResponseDTO> tracks;

    public PlaylistDTO() {
    }

    public PlaylistDTO(int id, String name, boolean owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public ArrayList<TrackResponseDTO> getTracks() {
        return tracks;
    }

    public void setTracks(ArrayList<TrackResponseDTO> tracks) {
        this.tracks = tracks;
    }
}
