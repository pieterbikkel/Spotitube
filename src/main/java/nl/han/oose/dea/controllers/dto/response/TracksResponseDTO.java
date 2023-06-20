package nl.han.oose.dea.controllers.dto.response;

import nl.han.oose.dea.controllers.dto.TrackDTO;

import java.util.ArrayList;

public class TracksResponseDTO {
    private ArrayList<TrackDTO> tracks = new ArrayList<>();

    public TracksResponseDTO() {
    }

    public TracksResponseDTO(ArrayList<TrackDTO> tracks) {
        this.tracks = tracks;
    }

    public ArrayList<TrackDTO> getTracks() {
        return tracks;
    }

    public void setTracks(ArrayList<TrackDTO> tracks) {
        this.tracks = tracks;
    }
}
