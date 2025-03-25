package org;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Path("/")
@ApplicationScoped
public class WebController {

    @Inject
    Template index;

    @Inject
    Template stats;

    @Inject
    UrlRepository urlRepository;

    @ConfigProperty(name = "url.shortener.domain")
    String domain;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a");

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response home() {
        return Response.ok(index.instance()).build();
    }

    @GET
    @Path("stats/view/{shortCode}")
    @Produces(MediaType.TEXT_HTML)
    public Response viewStats(@PathParam("shortCode") String shortCode) {
        Optional<UrlMapping> urlMapping = urlRepository.findByShortCode(shortCode);

        if (urlMapping.isPresent()) {
            UrlMapping mapping = urlMapping.get();

            // Format dates
            String formattedCreatedAt = formatDateTime(mapping.createdAt);
            String formattedExpiresAt = formatDateTime(mapping.expiresAt);
            String formattedLastAccessedAt = mapping.lastAccessedAt != null ?
                    formatDateTime(mapping.lastAccessedAt) : "Never";

            TemplateInstance template = stats.data("shortCode", mapping.shortCode)
                    .data("originalUrl", mapping.originalUrl)
                    .data("createdAt", formattedCreatedAt)
                    .data("expiresAt", formattedExpiresAt)
                    .data("accessCount", mapping.accessCount)
                    .data("lastAccessedAt", formattedLastAccessedAt)
                    .data("shortUrlBase", domain);

            return Response.ok(template).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("URL not found or has expired")
                    .build();
        }
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "N/A";
        }
        return dateTime.format(DATE_FORMATTER);
    }
}