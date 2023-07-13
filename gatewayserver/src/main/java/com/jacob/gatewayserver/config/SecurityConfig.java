package com.jacob.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityChain(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> exchanges.pathMatchers("/market/products/**").permitAll()
                .pathMatchers("/market/users/**").permitAll()
                .pathMatchers("/market/orders/**").authenticated())
                .oauth2ResourceServer().jwt();
        http.csrf().disable();
        return http.build();
    }
}
