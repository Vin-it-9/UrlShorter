package org;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;
import java.util.Optional;

@Path("/")
public class UrlController {

    @Inject
    UrlService urlService;

    @POST
    @Path("/shorten")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response shortenUrl(UrlRequest request) {
        if (request.url == null || request.url.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("URL cannot be empty"))
                    .build();
        }

        int expirationDays = request.expirationDays != null ? request.expirationDays : 0;
        String shortenedUrl = urlService.shortenUrl(request.url, expirationDays);

        return Response.ok(new UrlResponse(shortenedUrl)).build();
    }

    @GET
    @Path("/{shortCode}")
    public Response redirect(@PathParam("shortCode") String shortCode) {
        Optional<String> originalUrl = urlService.getOriginalUrl(shortCode);

        if (originalUrl.isPresent()) {
            URI uri = UriBuilder.fromUri(originalUrl.get()).build();
            return Response.temporaryRedirect(uri).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("URL not found or has expired")
                    .build();
        }
    }

    public static class UrlRequest {
        public String url;
        public Integer expirationDays;
    }

    public static class UrlResponse {
        public String shortenedUrl;

        public UrlResponse(String shortenedUrl) {
            this.shortenedUrl = shortenedUrl;
        }
    }

    public static class ErrorResponse {
        public String error;

        public ErrorResponse(String error) {
            this.error = error;
        }
    }

}