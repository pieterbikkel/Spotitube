package nl.han.oose.dea.datasource.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ForbiddenResourceExceptionMapper implements ExceptionMapper<ForbiddenResourceException> {
    @Override
    public Response toResponse(ForbiddenResourceException e) {
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
