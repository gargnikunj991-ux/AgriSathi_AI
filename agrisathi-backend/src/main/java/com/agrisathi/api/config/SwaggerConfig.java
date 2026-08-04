package com.agrisathi.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI 3.0 & Swagger Configuration for AgriSathi AI API documentation.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
                .info(new Info()
                        .title("AgriSathi AI - Smart Agricultural Advisory Platform REST APIs")
                        .description("""
                                ## AgriSathi AI REST API Suite
                                
                                AgriSathi AI is an intelligent digital farming platform empowering farmers with:
                                - **Authentication & User Management**: Secure JWT-based registration and profile context.
                                - **Farmer Profiles & Crop Tracking**: Farm size, soil types, sowing, and harvest schedules.
                                - **AI Disease Scans & Diagnostics**: Automated plant pathology detection and advisory via Cloudinary media.
                                - **Hyper-Local Weather Forecasts & Agromet Summaries**: Real-time Open-Meteo forecasts and irrigation/spraying guidance.
                                - **Government Scheme Discovery & Recommendation**: Matching state/national subsidies and benefits.
                                - **Produce Marketplace & Direct Buyer Connect**: Peer-to-peer crop listing and contact generation.
                                - **AI Conversational Advisory & Fertilizer Planning**: Context-aware NPK recommendation and Gemini AI chat.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("AgriSathi AI Engineering Team")
                                .email("support@agrisathi-ai.com")
                                .url("https://agrisathi-ai.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development Environment"),
                        new Server().url("https://api.agrisathi-ai.com").description("Production Server Environment")
                ))
                .tags(List.of(
                        new Tag().name("Authentication APIs").description("Endpoints for user registration, login, and profile context"),
                        new Tag().name("Farmer Profile APIs").description("Manage farmer demographic, soil, location, and farm details"),
                        new Tag().name("Crop APIs").description("Crop inventory, planting schedules, and harvest tracking"),
                        new Tag().name("Disease Detection APIs").description("AI-powered crop disease scan, diagnosis, and history tracking"),
                        new Tag().name("Weather APIs").description("Real-time weather, 7-day forecast, and farming advisory summary"),
                        new Tag().name("Recommendation APIs").description("Fertilizer recommendations and crop cultivation guidance"),
                        new Tag().name("AI Chat APIs").description("Interactive AI assistant for agricultural queries"),
                        new Tag().name("Marketplace APIs").description("Farmer produce marketplace, search, listings, and seller contacts"),
                        new Tag().name("Government Schemes APIs").description("National and state government agricultural scheme catalog and AI matching"),
                        new Tag().name("File Upload APIs").description("Media storage and image file upload management")
                ))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Provide JWT bearer token obtained from POST /api/v1/auth/login")));
    }
}
