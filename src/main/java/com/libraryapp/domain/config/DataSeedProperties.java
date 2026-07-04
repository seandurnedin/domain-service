package com.libraryapp.domain.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Controls the one-time startup data seed (sample books + an admin and a manager user). Disabled
 * by default - flip app.data-seed.enabled=true (or DATA_SEED_ENABLED=true) for a fresh database.
 * The seeder itself also checks for existing data before inserting anything, so leaving this flag
 * on across restarts is safe - it won't create duplicates.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.data-seed")
public class DataSeedProperties {

    private boolean enabled;
    private String adminUsername;
    private String adminEmail;
    private String adminPassword;
    private String managerUsername;
    private String managerEmail;
    private String managerPassword;
}
