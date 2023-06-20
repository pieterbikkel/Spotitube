package nl.han.oose.dea.services;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.controllers.dto.request.LoginRequestDTO;
import nl.han.oose.dea.controllers.dto.response.LoginResponseDTO;
import nl.han.oose.dea.datasource.dao.LoginDAO;
import nl.han.oose.dea.datasource.exceptions.InvalidCredentialsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginServiceTest {
    // System Under Test
    private LoginService sut;
    private LoginDAO loginDAO;
    private LoginRequestDTO loginRequestDTO;
    private String token = "test-token";
    private LoginResponseDTO loginResponseDTO;

    @BeforeEach
    public void setup() {
        var user = "user";
        var password = "password";
        var token = "token";

        sut = new LoginService();
        loginDAO = mock(LoginDAO.class);
        sut.setLoginDAO(loginDAO);
        loginRequestDTO = new LoginRequestDTO();
        loginResponseDTO = new LoginResponseDTO(user, token);
    }

    @Test
    void generateTokenSuccess() {
        Assertions.assertNotNull(sut.generateToken());
    }

    @Test
    void verifyTokenNoException() {
        try {
            // Act
            sut.verifyToken(token);
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    void checkCredentialsSuccess() {
        var expectedResult = Response.Status.OK;
        try {
            when(loginDAO.userExists(loginRequestDTO)).thenReturn(true);

            var result = sut.checkCredentials(loginRequestDTO);
            Assertions.assertEquals(expectedResult.getStatusCode(), result.getStatus());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void checkCredentialsInvalid() {
        try {
            when(loginDAO.userExists(loginRequestDTO)).thenReturn(false);
            Assertions.assertThrows(InvalidCredentialsException.class, () -> sut.checkCredentials(loginRequestDTO));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void checkCredentialsBadRequest() {
        try {
            doThrow(Exception.class).when(loginDAO).userExists(loginRequestDTO);
            Assertions.assertThrows(BadRequestException.class, () -> sut.checkCredentials(loginRequestDTO));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void buildSuccessResponseSuccess() {
        var expectedResponse = Response.Status.OK.getStatusCode();

        Response response = sut.buildSuccessResponse(loginRequestDTO.getUser(), token);

        Assertions.assertEquals(expectedResponse, response.getStatus());
    }

}
