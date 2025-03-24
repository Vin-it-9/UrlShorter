package org;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "url_mappings", indexes = {
        @Index(name = "idx_shortcode", columnList = "shortCode", unique = true)
})
public class UrlMapping extends PanacheEntity {

    @Column(nullable = false, length = 2048)
    public String originalUrl;

    @Column(nullable = false, unique = true, length = 20)
    public String shortCode;

    @Column(nullable = false)
    public LocalDateTime createdAt;

    @Column
    public LocalDateTime expiresAt;

    @Column
    public Long accessCount = 0L;

    @Column
    public LocalDateTime lastAccessedAt;

    public UrlMapping() {
        this.createdAt = LocalDateTime.now();
    }

    public UrlMapping(String originalUrl, String shortCode) {
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.createdAt = LocalDateTime.now();
    }
}