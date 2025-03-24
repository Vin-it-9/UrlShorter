package org;




import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@Path("/stats")
@Produces(MediaType.APPLICATION_JSON)
public class StatsController {

    @Inject
    UrlRepository urlRepository;

    @GET
    @Path("/{shortCode}")
    public Response getUrlStats(@PathParam("shortCode") String shortCode) {
        Optional<UrlMapping> urlMapping = urlRepository.findByShortCode(shortCode);

        if (urlMapping.isPresent()) {
            UrlMapping mapping = urlMapping.get();

            UrlStats stats = new UrlStats();
            stats.shortCode = mapping.shortCode;
            stats.originalUrl = mapping.originalUrl;
            stats.createdAt = mapping.createdAt;
            stats.expiresAt = mapping.expiresAt;
            stats.accessCount = mapping.accessCount;
            stats.lastAccessedAt = mapping.lastAccessedAt;

            return Response.ok(stats).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("URL not found"))
                    .build();
        }
    }

    public static class UrlStats {
        public String shortCode;
        public String originalUrl;
        public java.time.LocalDateTime createdAt;
        public java.time.LocalDateTime expiresAt;
        public Long accessCount;
        public java.time.LocalDateTime lastAccessedAt;
    }

    public static class ErrorResponse {
        public String error;

        public ErrorResponse(String error) {
            this.error = error;
        }
    }
}