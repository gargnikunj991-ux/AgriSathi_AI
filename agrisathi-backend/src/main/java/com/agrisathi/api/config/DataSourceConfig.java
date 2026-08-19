package com.agrisathi.api.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

@Slf4j
@Configuration
public class DataSourceConfig {

    @Value("${DB_URL:${DATABASE_URL:${spring.datasource.url:}}}")
    private String rawUrl;

    @Value("${DB_USERNAME:${spring.datasource.username:}}")
    private String configuredUsername;

    @Value("${DB_PASSWORD:${spring.datasource.password:}}")
    private String configuredPassword;

    @Value("${DB_POOL_MAX:${spring.datasource.hikari.maximum-pool-size:5}}")
    private int maxPoolSize;

    @Value("${DB_POOL_MIN:${spring.datasource.hikari.minimum-idle:1}}")
    private int minIdle;

    @Bean
    @Primary
    public DataSource dataSource() {
        return new HikariDataSource(buildHikariConfig());
    }

    public HikariConfig buildHikariConfig() {
        HikariConfig config = new HikariConfig();
        
        String url = rawUrl != null ? rawUrl.trim() : "";
        String username = configuredUsername != null ? configuredUsername.trim() : "";
        String password = configuredPassword != null ? configuredPassword.trim() : "";

        if (url.isEmpty()) {
            log.warn("No database URL provided (DB_URL or DATABASE_URL). Using in-memory H2 database fallback.");
            config.setJdbcUrl("jdbc:h2:mem:agrisathidb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL");
            config.setDriverClassName("org.h2.Driver");
            config.setUsername("sa");
            config.setPassword("");
        } else {
            String workingUrl = url;
            if (workingUrl.startsWith("jdbc:")) {
                workingUrl = workingUrl.substring(5);
            }

            if (workingUrl.startsWith("postgres://") || workingUrl.startsWith("postgresql://")) {
                try {
                    // Extract query parameters if present
                    String query = "";
                    int queryIdx = workingUrl.indexOf('?');
                    if (queryIdx != -1) {
                        query = workingUrl.substring(queryIdx);
                        workingUrl = workingUrl.substring(0, queryIdx);
                    }

                    // Remove postgres:// or postgresql://
                    String rest = workingUrl.replaceFirst("^postgres(ql)?://", "");

                    // Separate authority (user:pass@host:port) and database path (/dbname)
                    int slashIdx = rest.indexOf('/');
                    String authority = (slashIdx != -1) ? rest.substring(0, slashIdx) : rest;
                    String dbPath = (slashIdx != -1) ? rest.substring(slashIdx) : "";
                    if (dbPath.isEmpty()) {
                        dbPath = "/";
                    }

                    // Check if credentials are embedded in authority (user:pass@host:port)
                    int atIdx = authority.lastIndexOf('@');
                    String hostPort = authority;
                    if (atIdx != -1) {
                        String userInfo = authority.substring(0, atIdx);
                        hostPort = authority.substring(atIdx + 1);

                        int colonIdx = userInfo.indexOf(':');
                        if (colonIdx != -1) {
                            username = userInfo.substring(0, colonIdx);
                            password = userInfo.substring(colonIdx + 1);
                        } else {
                            username = userInfo;
                        }
                    }

                    // Default to port 5432 if no port is specified in hostPort
                    if (!hostPort.contains(":")) {
                        hostPort = hostPort + ":5432";
                    }

                    String cleanJdbcUrl = "jdbc:postgresql://" + hostPort + dbPath + query;
                    config.setJdbcUrl(cleanJdbcUrl);
                    config.setDriverClassName("org.postgresql.Driver");
                    config.setUsername(username);
                    config.setPassword(password);

                    log.info("Configured PostgreSQL connection to host: {}, database: {}", hostPort, dbPath);
                } catch (Exception e) {
                    log.error("Failed to parse PostgreSQL URL: {}. Fallback to raw.", url, e);
                    config.setJdbcUrl(url.startsWith("jdbc:") ? url : "jdbc:" + url);
                    config.setDriverClassName("org.postgresql.Driver");
                    config.setUsername(username);
                    config.setPassword(password);
                }
            } else {
                config.setJdbcUrl(url);
                if (url.contains("postgresql")) {
                    config.setDriverClassName("org.postgresql.Driver");
                }
                config.setUsername(username);
                config.setPassword(password);
            }
        }

        config.setMaximumPoolSize(maxPoolSize);
        config.setMinimumIdle(minIdle);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(20000);
        config.setMaxLifetime(1800000);

        return config;
    }
}
