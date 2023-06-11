package nl.han.oose.dea.services;

import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.controllers.dto.request.LoginRequestDTO;
import nl.han.oose.dea.datasource.dao.LoginDAO;
import nl.han.oose.dea.datasource.exceptions.InvalidCredentialsException;

public class LoginService {

    private LoginDAO loginDAO;

    public Response checkCredentials(LoginRequestDTO loginRequestDTO) {
        try {
            if (loginDAO.userExists(loginRequestDTO)) {
                loginDAO.generateToken(loginRequestDTO);
                return Response
                        .status(Response.Status.OK)
                        .entity(loginDAO.checkUserCredentials(loginRequestDTO))
                        .build();
            } else {
                throw new InvalidCredentialsException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException();
        }
    }

    @Inject
    public void setLoginDAO(LoginDAO loginDAO) {
        this.loginDAO = loginDAO;
    }
}
