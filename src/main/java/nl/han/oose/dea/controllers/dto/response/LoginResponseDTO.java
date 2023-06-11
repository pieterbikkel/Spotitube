package nl.han.oose.dea.controllers.dto.response;

public class LoginResponseDTO {

    private String gebruikersnaam, token;

    public LoginResponseDTO() {}

    public LoginResponseDTO(String gebruikersnaam, String token) {
        this.gebruikersnaam = gebruikersnaam;
        this.token = token;
    }

    public String getGebruikersnaam() {
        return gebruikersnaam;
    }

    public void setGebruikersnaam(String gebruikersnaam) {
        this.gebruikersnaam = gebruikersnaam;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
