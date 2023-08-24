package de.bsi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

@Configuration
public class SecurityConfigurationNew {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/oauth2/authorization/trsso", "/loginFailure", "/favicon.ico","/", "/error", "/webjars/**")//oauth_login
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .loginPage("/oauth2/authorization/trsso")///oauth_login
                .defaultSuccessUrl("/test1")
                .failureUrl("/loginFailure");

        return http.build();
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }
}
