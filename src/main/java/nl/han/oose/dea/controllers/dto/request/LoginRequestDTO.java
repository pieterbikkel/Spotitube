package nl.han.oose.dea.controllers.dto.request;

public class LoginRequestDTO {

    private String user, password;

    public LoginRequestDTO() {}

    public LoginRequestDTO(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
