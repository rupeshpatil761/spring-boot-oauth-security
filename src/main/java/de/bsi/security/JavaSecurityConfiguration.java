package de.bsi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;

//@EnableWebSecurity // Not required really
@Configuration
public class JavaSecurityConfiguration {

    // IMPORTANT: Make sure disable the YML Based and also comment SecurityConfiguration.java @config annotation

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests()
        .antMatchers("/login").permitAll().anyRequest().authenticated();
        httpSecurity.oauth2Login().loginProcessingUrl("/login");
        return httpSecurity.build();
    }

    @Bean
    public ClientRegistrationRepository getTrSsoRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.trSsoClientRegistration());
    }

    private ClientRegistration trSsoClientRegistration() {
        return ClientRegistration.withRegistrationId("sso")
                .issuerUri("https://sso.tr.com")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientId(retrieveClientId())
                .clientSecret(retrieveClientSecret())
                .redirectUri(retrieveRedirectUrl())
                .scope("openid")
                .authorizationUri("https://sso.tr.com/as/authorization.oauth2")
                .tokenUri("https://sso.tr.com/as/token.oauth2")
                .jwkSetUri("https://sso.tr.com/pf/JWKS")
                .userInfoUri("https://sso.tr.com/idp/userinfo.openid")
                .redirectUri("http://localhost:8080/login") //same must be configured in client sso config
                .userNameAttributeName("sub")
                .build();
    }

    private String retrieveClientId(){
        // fetch from secrets manager
        return "";
    }

    private String retrieveClientSecret(){
        // fetch from secrets manager
        return "";
    }

    private String retrieveRedirectUrl(){
        // fetch from secrets manager
        return "http://localhost:8080/login";
    }
}
