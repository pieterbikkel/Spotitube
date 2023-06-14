package nl.han.oose.dea.controllers.dto.response;

public class LoginResponseDTO {

    private String user, token;

    public LoginResponseDTO() {}

    public LoginResponseDTO(String gebruikersnaam, String token) {
        this.user = gebruikersnaam;
        this.token = token;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
