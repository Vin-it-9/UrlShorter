package org;


import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class UrlRepository implements PanacheRepository<UrlMapping> {

    public Optional<UrlMapping> findByShortCode(String shortCode) {
        return find("shortCode", shortCode).firstResultOptional();
    }

    public boolean shortCodeExists(String shortCode) {
        return count("shortCode", shortCode) > 0;
    }

    public Optional<UrlMapping> findByOriginalUrl(String originalUrl) {
        return find("originalUrl", originalUrl).firstResultOptional();
    }

    public void incrementAccessCount(UrlMapping urlMapping) {
        urlMapping.accessCount++;
        urlMapping.lastAccessedAt = LocalDateTime.now();
        persist(urlMapping);
    }


}