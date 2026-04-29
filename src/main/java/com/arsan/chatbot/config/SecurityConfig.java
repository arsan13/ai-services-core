package com.arsan.chatbot.config;

import com.arsan.chatbot.enums.PermissionType;
import com.arsan.chatbot.properties.SecurityProperties;
import com.arsan.chatbot.security.handler.OAuth2FailureHandler;
import com.arsan.chatbot.security.handler.OAuth2SuccessHandler;
import com.arsan.chatbot.security.jwt.JwtAuthFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@ConditionalOnProperty(name = "app.security.enabled", havingValue = "true", matchIfMissing = true)
//@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityProperties securityProperties;
    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String[] publicPaths = securityProperties.getPublicEndpoints().toArray(new String[0]);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)) // For H2 console. To be removed
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(publicPaths).permitAll()

                        .requestMatchers(HttpMethod.GET, "/admin/token-usage/**").hasAuthority(PermissionType.TOKEN_USAGE_READ.getAuthority())
                        .requestMatchers(HttpMethod.GET, "/admin/users/**").hasAuthority(PermissionType.ADMIN_READ.getAuthority())
                        .requestMatchers(HttpMethod.GET, "/admin/users/permission/available").hasAuthority(PermissionType.ADMIN_READ.getAuthority())
                        .requestMatchers(HttpMethod.PATCH, "/admin/users/**").hasAuthority(PermissionType.ADMIN_WRITE.getAuthority())

                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oAuth2 -> oAuth2
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler)
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(this::handleException)
                        .accessDeniedHandler(this::handleException)
                );

        return http.build();
    }

    private void handleException(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        handlerExceptionResolver.resolveException(request, response, null, exception);
    }
}
