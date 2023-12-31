package nl.han.oose.dea.datasource.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidCredentialsExceptionMapper implements ExceptionMapper<InvalidCredentialsException> {
    @Override
    public Response toResponse(InvalidCredentialsException e) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
