package com.yvolabs.airbnbclone.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 17/09/2024
 */

@Configuration
@EnableJpaRepositories({
        "com.yvolabs.airbnbclone.user.repository",
        "com.yvolabs.airbnbclone.listing.repository",
        "com.yvolabs.airbnbclone.booking.repository"
})
@EnableTransactionManagement
@EnableJpaAuditing
public class DatabaseConfiguration {
}
