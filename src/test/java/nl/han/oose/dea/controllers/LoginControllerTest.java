package nl.han.oose.dea.controllers;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.controllers.dto.request.LoginRequestDTO;
import nl.han.oose.dea.controllers.dto.response.LoginResponseDTO;
import nl.han.oose.dea.datasource.exceptions.InvalidCredentialsException;
import nl.han.oose.dea.services.LoginService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginControllerTest {
    private LoginController sut;
    private LoginService loginService;
    private LoginRequestDTO loginRequestDTO;

    @BeforeEach
    public void setup() {
        var user = "user";
        var password = "password";

        sut = new LoginController();
        loginService = mock(LoginService.class);
        sut.setLoginService(loginService);

        loginRequestDTO = new LoginRequestDTO(user, password);
    }

    @Test
    void LoginSuccess() {
        var expectedResult = Response.Status.OK;
        when(loginService.checkCredentials(loginRequestDTO)).thenReturn(Response.status(expectedResult).build());

        var result = sut.login(loginRequestDTO);

        Assertions.assertEquals(expectedResult.getStatusCode(), result.getStatus());
    }

    @Test
    void LoginInvalidCredentials() {
        var expectedResult = new InvalidCredentialsException();
        when(loginService.checkCredentials(loginRequestDTO)).thenThrow(expectedResult);

        Assertions.assertThrows(InvalidCredentialsException.class, () -> sut.login(loginRequestDTO));
    }

    @Test
    void LoginBadRequest() {
        var expectedResult = new BadRequestException();
        when(loginService.checkCredentials(loginRequestDTO)).thenThrow(expectedResult);

        Assertions.assertThrows(BadRequestException.class, () -> sut.login(loginRequestDTO));
    }

}
