package com.portfolio.api.image;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "storage")
public record StorageProperties(
    String provider,
    String bucket,
    String region,
    String accessKey,
    String secretKey,
    String endpoint,
    String publicBaseUrl
) {
}
