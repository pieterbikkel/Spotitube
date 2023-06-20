package nl.han.oose.dea.services;

import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.controllers.dto.request.LoginRequestDTO;
import nl.han.oose.dea.controllers.dto.response.LoginResponseDTO;
import nl.han.oose.dea.datasource.dao.LoginDAO;
import nl.han.oose.dea.datasource.exceptions.ForbiddenResourceException;
import nl.han.oose.dea.datasource.exceptions.InvalidCredentialsException;
import java.util.UUID;

public class LoginService {
    @Inject
    private LoginDAO loginDAO;

    public Response checkCredentials(LoginRequestDTO loginRequestDTO) {
        try {
            if (loginDAO.userExists(loginRequestDTO)) {
                var token = generateToken();
                loginDAO.updateToken(loginRequestDTO, token);
                return buildSuccessResponse(loginRequestDTO.getUser(), token);
            } else {
                throw new InvalidCredentialsException();
            }
        } catch (InvalidCredentialsException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException();
        }
    }

    public Response buildSuccessResponse(String user, String token) {
        var loginResponseDTO = new LoginResponseDTO(user, token);
        return Response
                .status(Response.Status.OK)
                .entity(loginResponseDTO)
                .build();
    }

    public void verifyToken(String token) throws ForbiddenResourceException {
        loginDAO.verifyToken(token);
    }

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Inject
    public void setLoginDAO(LoginDAO loginDAO) {
        this.loginDAO = loginDAO;
    }
}
