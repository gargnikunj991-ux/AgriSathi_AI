package com.agrisathi.api;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AgriSathiApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(AgriSathiApplication.class);
        Map<String, Object> defaultProperties = databaseDefaultsFromEnvironment(System.getenv());
        if (!defaultProperties.isEmpty()) {
            application.setDefaultProperties(defaultProperties);
        }
        application.run(args);
    }

    static Map<String, Object> databaseDefaultsFromEnvironment(Map<String, String> environment) {
        if (hasText(environment.get("DB_URL"))) {
            return Map.of();
        }

        String databaseUrl = environment.get("DATABASE_URL");
        if (!hasText(databaseUrl)) {
            return Map.of();
        }

        URI uri = URI.create(databaseUrl);
        String scheme = uri.getScheme();
        if (!"postgresql".equalsIgnoreCase(scheme) && !"postgres".equalsIgnoreCase(scheme)) {
            return Map.of();
        }

        String path = uri.getRawPath();
        if (!hasText(uri.getHost()) || !hasText(path) || "/".equals(path)) {
            return Map.of();
        }

        Map<String, Object> defaults = new HashMap<>();
        String port = uri.getPort() > 0 ? ":" + uri.getPort() : "";
        String query = hasText(uri.getRawQuery()) ? "?" + uri.getRawQuery() : "";
        defaults.put("DB_URL", "jdbc:postgresql://" + uri.getHost() + port + path + query);

        String userInfo = uri.getRawUserInfo();
        if (hasText(userInfo)) {
            int separator = userInfo.indexOf(':');
            if (separator >= 0) {
                putIfMissing(defaults, environment, "DB_USERNAME", decode(userInfo.substring(0, separator)));
                putIfMissing(defaults, environment, "DB_PASSWORD", decode(userInfo.substring(separator + 1)));
            } else {
                putIfMissing(defaults, environment, "DB_USERNAME", decode(userInfo));
            }
        }

        return defaults;
    }

    private static void putIfMissing(
            Map<String, Object> defaults,
            Map<String, String> environment,
            String key,
            String value) {
        if (!hasText(environment.get(key)) && hasText(value)) {
            defaults.put(key, value);
        }
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
