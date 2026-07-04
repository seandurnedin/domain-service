package com.libraryapp.domain.config;

import com.libraryapp.domain.entity.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * To expose IDs in JSON Payload
 */
@Configuration
public class RestConfig implements RepositoryRestConfigurer {

    @Value("${spring.data.rest.base-path}")
    private String basePath;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        config.exposeIdsFor(
                User.class,
                UserGroup.class,
                Book.class,
                BorrowingRecord.class,
                Reservation.class,
                Payment.class
        );
        config.setBasePath(basePath);
    }
}
