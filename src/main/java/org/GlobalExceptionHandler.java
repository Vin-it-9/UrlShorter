package org;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        String htmlErrorMessage = "<html><body><h1>Error</h1><p>An error occurred: " +
                exception.getMessage() + "</p></body></html>";

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(htmlErrorMessage)
                .type(MediaType.TEXT_HTML_TYPE)
                .build();
    }

}