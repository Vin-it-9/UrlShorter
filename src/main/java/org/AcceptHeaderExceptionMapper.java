package org;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AcceptHeaderExceptionMapper implements ExceptionMapper<BadRequestException> {

    @Override
    public Response toResponse(BadRequestException exception) {
        if (exception.getMessage().contains("Malformed quality value")) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("<html><body><h1>Bad Request</h1><p>Invalid Accept header format in your request.</p></body></html>")
                    .type(MediaType.TEXT_HTML)
                    .build();
        }
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("<html><body><h1>Bad Request</h1><p>" + exception.getMessage() + "</p></body></html>")
                .type(MediaType.TEXT_HTML)
                .build();
    }
}