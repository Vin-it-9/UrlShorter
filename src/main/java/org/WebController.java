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

            TemplateInstance template = stats.data("shortCode", mapping.shortCode)
                    .data("originalUrl", mapping.originalUrl)
                    .data("createdAt", mapping.createdAt)
                    .data("expiresAt", mapping.expiresAt)
                    .data("accessCount", mapping.accessCount)
                    .data("lastAccessedAt", mapping.lastAccessedAt)
                    .data("shortUrlBase", domain);

            return Response.ok(template).build();

        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("URL not found or has expired")
                    .build();
        }
    }
}