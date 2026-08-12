package com.mari.flora.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@ConfigurationProperties(prefix = "app.image")
@Getter
@Setter
public class ImageStorageProperties {

    private String uploadDir;

    /** app.image.allowed-extensions is a comma-separated string, bound here as a Set for O(1) lookup */
    private Set<String> allowedExtensions;

    private long maxSizeBytes;
}