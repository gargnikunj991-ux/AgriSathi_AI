package com.agrisathi.api.config;

import com.agrisathi.api.security.JwtAuthenticationEntryPoint;
import com.agrisathi.api.security.JwtAuthenticationFilter;
import com.agrisathi.api.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfig corsConfig;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)) // For H2 console
            .authorizeHttpRequests(auth -> auth
                // 1. STRICTLY PUBLIC ENDPOINTS (Register, Login, Health & Docs)
                .requestMatchers("/", "/api/v1/health", "/api/v1/auth/register", "/api/v1/auth/login").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/h2-console/**").permitAll()

                // 2. ADMIN / OWNER ONLY ENDPOINTS (Master control; cannot modify user personal info)
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/government-schemes").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/government-schemes/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/government-schemes/*").hasRole("ADMIN")

                // 3. FARMER ONLY PERSONAL PROFILE (Admin cannot alter user personal info/profile)
                .requestMatchers("/api/v1/farmer/profile/**").hasRole("FARMER")

                // 4. FARMING RELATED FEATURES (Accessible to Farmer and Admin)
                .requestMatchers("/api/v1/crops/**").hasAnyRole("FARMER", "ADMIN")
                .requestMatchers("/api/v1/disease/**").hasAnyRole("FARMER", "ADMIN")
                .requestMatchers("/api/v1/recommendations/**").hasAnyRole("FARMER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/weather/**").hasAnyRole("FARMER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/government-schemes/**").hasAnyRole("FARMER", "ADMIN")
                .requestMatchers("/api/v1/chat/**").hasAnyRole("FARMER", "ADMIN")
                .requestMatchers("/api/v1/files/**").hasAnyRole("FARMER", "ADMIN")

                // 5. MARKETPLACE MANAGEMENT (Farmer & Admin create, update, delete listings & view my-listings)
                .requestMatchers(HttpMethod.POST, "/api/v1/marketplace/listings").hasAnyRole("FARMER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/marketplace/listings/*").hasAnyRole("FARMER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/marketplace/listings/*").hasAnyRole("FARMER", "ADMIN")
                .requestMatchers("/api/v1/marketplace/my-listings").hasAnyRole("FARMER", "ADMIN")

                // 6. BUYER SPECIFIC ACCESSIBLE ENDPOINTS (Marketplace view, search, contact seller, buy, borrow, & auth me)
                .requestMatchers(HttpMethod.GET, "/api/v1/marketplace/listings", "/api/v1/marketplace/listings/**", "/api/v1/marketplace/listings/search").hasAnyRole("BUYER", "FARMER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/marketplace/listings/*/contact").hasAnyRole("BUYER", "FARMER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/marketplace/listings/*/buy").hasAnyRole("BUYER", "FARMER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/marketplace/listings/*/borrow").hasAnyRole("BUYER", "FARMER", "ADMIN")
                .requestMatchers("/api/v1/auth/me").hasAnyRole("BUYER", "FARMER", "ADMIN")

                // 7. ALL OTHER ENDPOINTS REQUIRE AUTHENTICATION
                .anyRequest().authenticated()
            );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
