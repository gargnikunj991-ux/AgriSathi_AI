package com.agrisathi.api.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Database Initializer to ensure schema constraints and existing data support modern role hierarchy (ROLE_ADMIN, ROLE_FARMER, ROLE_BUYER).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        try {
            // 1. Drop legacy check constraint if present
            jdbcTemplate.execute("ALTER TABLE users DROP CONSTRAINT IF EXISTS users_role_check");
            // 2. Migrate legacy ROLE_USER data to ROLE_FARMER
            jdbcTemplate.execute("UPDATE users SET role = 'ROLE_FARMER' WHERE role = 'ROLE_USER'");
            log.info("Database constraint check and role data migration completed successfully.");
        } catch (Exception ex) {
            log.warn("Notice: Could not alter users table: {}", ex.getMessage());
        }
    }
}
