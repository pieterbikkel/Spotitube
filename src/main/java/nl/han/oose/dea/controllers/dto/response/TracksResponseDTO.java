package nl.han.oose.dea.controllers.dto.response;

import java.util.ArrayList;

public class TracksResponseDTO {
    private ArrayList<TrackResponseDTO> tracks = new ArrayList<>();

    public TracksResponseDTO() {
    }

    public TracksResponseDTO(ArrayList<TrackResponseDTO> tracks) {
        this.tracks = tracks;
    }

    public ArrayList<TrackResponseDTO> getTracks() {
        return tracks;
    }

    public void setTracks(ArrayList<TrackResponseDTO> tracks) {
        this.tracks = tracks;
    }
}
