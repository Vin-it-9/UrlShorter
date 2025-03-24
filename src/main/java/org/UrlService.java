package org;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class UrlService {

    @Inject
    UrlRepository urlRepository;

    @ConfigProperty(name = "url.shortener.domain")
    String domain;

    @ConfigProperty(name = "url.shortener.length", defaultValue = "6")
    int shortCodeLength;

    private static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final SecureRandom random = new SecureRandom();

    @Transactional
    public String shortenUrl(String originalUrl) {
        Optional<UrlMapping> existingUrl = urlRepository.findByOriginalUrl(originalUrl);
        if (existingUrl.isPresent()) {
            return domain + existingUrl.get().shortCode;
        }
        String shortCode = generateUniqueShortCode();

        UrlMapping urlMapping = new UrlMapping(originalUrl, shortCode);
        urlRepository.persist(urlMapping);

        return domain + shortCode;
    }

    @Transactional
    public String shortenUrl(String originalUrl, int expirationDays) {
        Optional<UrlMapping> existingUrl = urlRepository.findByOriginalUrl(originalUrl);
        if (existingUrl.isPresent()) {
            return domain + existingUrl.get().shortCode;
        }

        String shortCode = generateUniqueShortCode();

        UrlMapping urlMapping = new UrlMapping(originalUrl, shortCode);

        if (expirationDays > 0) {
            urlMapping.expiresAt = LocalDateTime.now().plusDays(expirationDays);
        }

        urlRepository.persist(urlMapping);

        return domain + shortCode;

    }

    @Transactional
    public Optional<String> getOriginalUrl(String shortCode) {
        Optional<UrlMapping> urlMapping = urlRepository.findByShortCode(shortCode);

        if (urlMapping.isPresent()) {
            UrlMapping mapping = urlMapping.get();

            if (mapping.expiresAt != null && mapping.expiresAt.isBefore(LocalDateTime.now())) {
                return Optional.empty();
            }

            urlRepository.incrementAccessCount(mapping);

            return Optional.of(mapping.originalUrl);
        }

        return Optional.empty();
    }

    private String generateUniqueShortCode() {
        String shortCode;
        do {
            shortCode = generateRandomShortCode();
        } while (urlRepository.shortCodeExists(shortCode));

        return shortCode;
    }

    private String generateRandomShortCode() {
        StringBuilder sb = new StringBuilder(shortCodeLength);
        for (int i = 0; i < shortCodeLength; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARS.length());
            sb.append(ALLOWED_CHARS.charAt(randomIndex));
        }
        return sb.toString();
    }

}