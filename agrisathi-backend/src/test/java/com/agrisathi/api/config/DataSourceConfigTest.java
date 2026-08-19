package com.agrisathi.api.config;

import com.zaxxer.hikari.HikariConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DataSourceConfig Unit Tests")
class DataSourceConfigTest {

    @Test
    @DisplayName("Should parse Render Internal Database URL with jdbc: prefix correctly")
    void testRenderInternalUrlWithJdbcPrefix() {
        DataSourceConfig config = new DataSourceConfig();
        ReflectionTestUtils.setField(config, "rawUrl", "jdbc:postgresql://agrisathi_db_user:NdMyt8QAKez6KPPd7vJqXSLCnYYeLYVT@dpg-da271u8u01pc73dvacm0-a/agrisathi_db");
        ReflectionTestUtils.setField(config, "configuredUsername", "");
        ReflectionTestUtils.setField(config, "configuredPassword", "");
        ReflectionTestUtils.setField(config, "maxPoolSize", 5);
        ReflectionTestUtils.setField(config, "minIdle", 1);

        HikariConfig hikariConfig = config.buildHikariConfig();
        assertEquals("jdbc:postgresql://dpg-da271u8u01pc73dvacm0-a:5432/agrisathi_db", hikariConfig.getJdbcUrl());
        assertEquals("agrisathi_db_user", hikariConfig.getUsername());
        assertEquals("NdMyt8QAKez6KPPd7vJqXSLCnYYeLYVT", hikariConfig.getPassword());
        assertEquals("org.postgresql.Driver", hikariConfig.getDriverClassName());
    }

    @Test
    @DisplayName("Should parse standard postgres:// URL with query parameters")
    void testStandardPostgresUrlWithQueryParams() {
        DataSourceConfig config = new DataSourceConfig();
        ReflectionTestUtils.setField(config, "rawUrl", "postgres://user123:pass456@dbhost.com:5432/agrisathidb?sslmode=require");
        ReflectionTestUtils.setField(config, "configuredUsername", "");
        ReflectionTestUtils.setField(config, "configuredPassword", "");
        ReflectionTestUtils.setField(config, "maxPoolSize", 5);
        ReflectionTestUtils.setField(config, "minIdle", 1);

        HikariConfig hikariConfig = config.buildHikariConfig();
        assertEquals("jdbc:postgresql://dbhost.com:5432/agrisathidb?sslmode=require", hikariConfig.getJdbcUrl());
        assertEquals("user123", hikariConfig.getUsername());
        assertEquals("pass456", hikariConfig.getPassword());
        assertEquals("org.postgresql.Driver", hikariConfig.getDriverClassName());
    }

    @Test
    @DisplayName("Should parse standard JDBC URL without user credentials")
    void testStandardJdbcUrl() {
        DataSourceConfig config = new DataSourceConfig();
        ReflectionTestUtils.setField(config, "rawUrl", "jdbc:postgresql://localhost:5432/agrisathidb");
        ReflectionTestUtils.setField(config, "configuredUsername", "postgres");
        ReflectionTestUtils.setField(config, "configuredPassword", "secret");
        ReflectionTestUtils.setField(config, "maxPoolSize", 5);
        ReflectionTestUtils.setField(config, "minIdle", 1);

        HikariConfig hikariConfig = config.buildHikariConfig();
        assertEquals("jdbc:postgresql://localhost:5432/agrisathidb", hikariConfig.getJdbcUrl());
        assertEquals("postgres", hikariConfig.getUsername());
        assertEquals("secret", hikariConfig.getPassword());
        assertEquals("org.postgresql.Driver", hikariConfig.getDriverClassName());
    }

    @Test
    @DisplayName("Should fallback to H2 when URL is empty")
    void testH2FallbackWhenUrlEmpty() {
        DataSourceConfig config = new DataSourceConfig();
        ReflectionTestUtils.setField(config, "rawUrl", "");
        ReflectionTestUtils.setField(config, "configuredUsername", "");
        ReflectionTestUtils.setField(config, "configuredPassword", "");
        ReflectionTestUtils.setField(config, "maxPoolSize", 5);
        ReflectionTestUtils.setField(config, "minIdle", 1);

        HikariConfig hikariConfig = config.buildHikariConfig();
        assertTrue(hikariConfig.getJdbcUrl().contains("jdbc:h2:mem"));
        assertEquals("sa", hikariConfig.getUsername());
    }
}
