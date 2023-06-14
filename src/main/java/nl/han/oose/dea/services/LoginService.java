package nl.han.oose.dea.services;

import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.controllers.dto.request.LoginRequestDTO;
import nl.han.oose.dea.controllers.dto.response.LoginResponseDTO;
import nl.han.oose.dea.datasource.dao.LoginDAO;
import nl.han.oose.dea.datasource.exceptions.InvalidCredentialsException;
import java.util.UUID;

public class LoginService {
    @Inject
    private LoginDAO loginDAO;

    public Response checkCredentials(LoginRequestDTO loginRequestDTO) {
        try {
            if (loginDAO.userExists(loginRequestDTO)) {
                var token = generateToken();
                loginDAO.generateToken(loginRequestDTO, token);
                var loginResponseDTO = new LoginResponseDTO(loginRequestDTO.getUser(), token);
                return Response
                        .status(Response.Status.OK)
                        .entity(loginResponseDTO)
                        .build();
            } else {
                throw new InvalidCredentialsException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException();
        }
    }

    public void verifyToken(String token) throws Exception {
        loginDAO.verifyToken(token);
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Inject
    public void setLoginDAO(LoginDAO loginDAO) {
        this.loginDAO = loginDAO;
    }
}
