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
        } else if (url.startsWith("postgres://") || url.startsWith("postgresql://")) {
            try {
                URI uri = new URI(url.replace("postgres://", "http://").replace("postgresql://", "http://"));
                String host = uri.getHost();
                int port = uri.getPort() == -1 ? 5432 : uri.getPort();
                String path = uri.getPath();
                String query = uri.getQuery() != null ? "?" + uri.getQuery() : "";
                
                String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + path + query;
                config.setJdbcUrl(jdbcUrl);
                config.setDriverClassName("org.postgresql.Driver");

                if (uri.getUserInfo() != null && !uri.getUserInfo().isEmpty()) {
                    String[] userInfo = uri.getUserInfo().split(":", 2);
                    config.setUsername(userInfo[0]);
                    if (userInfo.length > 1) {
                        config.setPassword(userInfo[1]);
                    }
                } else {
                    config.setUsername(username);
                    config.setPassword(password);
                }
                log.info("Configured PostgreSQL connection to host: {}, database: {}", host, path);
            } catch (Exception e) {
                log.error("Failed to parse PostgreSQL URL: {}. Using raw URL.", url, e);
                config.setJdbcUrl(url);
                config.setUsername(username);
                config.setPassword(password);
            }
        } else {
            config.setJdbcUrl(url);
            config.setUsername(username);
            config.setPassword(password);
        }

        config.setMaximumPoolSize(maxPoolSize);
        config.setMinimumIdle(minIdle);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(20000);
        config.setMaxLifetime(1800000);

        return new HikariDataSource(config);
    }
}
