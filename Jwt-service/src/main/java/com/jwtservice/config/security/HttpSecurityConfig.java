package com.jwtservice.config.security;

import com.jwtservice.util.RolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

                    authorizeRequests.requestMatchers(HttpMethod.GET,"/products")
                            .hasAuthority(RolePermission.READ_ALL_PRODUCTS.name());

                    authorizeRequests.requestMatchers(HttpMethod.GET,"/products/{productsId}")
                            .hasAuthority(RolePermission.READ_ONE_PRODUCT.name());

                    authorizeRequests.requestMatchers(HttpMethod.POST,"/products")
                            .hasAuthority(RolePermission.CREATE_ONE_PRODUCT.name());

                    authorizeRequests.requestMatchers(HttpMethod.PUT,"/products/{productsId}")
                            .hasAuthority(RolePermission.UPDATE_ONE_PRODUCT.name());

                    authorizeRequests.requestMatchers(HttpMethod.PUT,"/products/{productsId}/disabled")
                            .hasAuthority(RolePermission.DISABLE_ONE_PRODUCT.name());

                    authorizeRequests.requestMatchers(HttpMethod.POST,"/customers").permitAll();
                    authorizeRequests.requestMatchers(HttpMethod.POST,"/auth/authenticate").permitAll();
                    authorizeRequests.requestMatchers(HttpMethod.GET,"/auth/validate-token").permitAll();
                    authorizeRequests.anyRequest().authenticated();
                })
                .build();
    }
}
