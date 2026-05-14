package com.ab.perfume.security.config;

import com.ab.perfume.security.jwt.JwtEntryPoint;
import com.ab.perfume.security.jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtEntryPoint jwtEntryPoint;

    private final JwtTokenFilter jwtTokenFilter;

    @Bean

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        @Bean

        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

            http.csrf(AbstractHttpConfigurer::disable);

            http.cors(Customizer.withDefaults());

            http.sessionManagement(session ->

                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            );

            http.authorizeHttpRequests(auth -> auth

                    // ===============================
                    // AUTH PÚBLICO
                    // ===============================

                    .requestMatchers("/auth/**").permitAll()

                    // ===============================
                    // CATÁLOGO PÚBLICO (SOLO GET)
                    // ===============================

                    .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/brands/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/images/lociones/**").permitAll()

                    // ===============================
                    // CHECKOUT PÚBLICO
                    // ===============================

                    .requestMatchers(HttpMethod.POST, "/api/orders").permitAll()

                    // ===============================
                    // LO DEMÁS REQUIERE ADMIN
                    // ===============================

                    .anyRequest().hasRole("ADMIN")

            );

            http.exceptionHandling(exception ->

                    exception.authenticationEntryPoint(jwtEntryPoint)

            );

            http.addFilterBefore(
                    jwtTokenFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

            return http.build();

        }

        http.exceptionHandling(exception ->
                exception.authenticationEntryPoint(jwtEntryPoint)
        );

        http.addFilterBefore(
                jwtTokenFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();

    }

}
