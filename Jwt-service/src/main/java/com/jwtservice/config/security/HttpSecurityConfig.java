package com.jwtservice.config.security;

import com.jwtservice.util.Role;
import com.jwtservice.util.RolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
@EnableWebSecurity
public class HttpSecurityConfig {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) //csrfConfigurer -> csrfConfigurer.disable()
                .sessionManagement( config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeRequests -> {

                    buildRequestMatches(authorizeRequests);
                })
                .build();
    }

    private static void buildRequestMatches(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizeRequests) {
        authorizeRequests.requestMatchers(HttpMethod.GET,"/products")
                .hasAnyRole(Role.ADMINISTRATOR.name(),Role.ASSISTANT_ADMINISTRATOR.name());

        authorizeRequests.requestMatchers(RegexRequestMatcher.regexMatcher(
                        HttpMethod.GET,
                        "/products/[0-9]*"
                ))
                .hasAuthority(RolePermission.READ_ONE_PRODUCT.name());
        //.hasAnyRole(Role.ADMINISTRATOR.name(),Role.ASSISTANT_ADMINISTRATOR.name());

        authorizeRequests.requestMatchers(HttpMethod.POST,"/products")
                .hasAuthority(RolePermission.CREATE_ONE_PRODUCT.name());
        //.hasAnyRole(Role.ADMINISTRATOR.name());

        authorizeRequests.requestMatchers(HttpMethod.PUT,"/products/{productsId}")
                .hasAuthority(RolePermission.UPDATE_ONE_PRODUCT.name());
        //.hasAnyRole(Role.ADMINISTRATOR.name());


        authorizeRequests.requestMatchers(HttpMethod.PUT,"/products/{productsId}/disabled")
                .hasAuthority(RolePermission.DISABLE_ONE_PRODUCT.name());
        //.hasAnyRole(Role.ADMINISTRATOR.name(),Role.ASSISTANT_ADMINISTRATOR.name());

        authorizeRequests.requestMatchers(HttpMethod.POST,"/customers").permitAll();
        authorizeRequests.requestMatchers(HttpMethod.POST,"/auth/authenticate").permitAll();
        authorizeRequests.requestMatchers(HttpMethod.GET,"/auth/validate-token").permitAll();
        authorizeRequests.anyRequest().authenticated();
    }
}

