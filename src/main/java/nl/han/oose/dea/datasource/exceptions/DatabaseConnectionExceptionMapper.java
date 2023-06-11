package nl.han.oose.dea.datasource.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class DatabaseConnectionExceptionMapper implements ExceptionMapper<DatabaseConnectionException> {

    @Override
    public Response toResponse(DatabaseConnectionException e) {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
