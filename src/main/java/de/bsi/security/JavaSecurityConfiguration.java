package de.bsi.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.westgroup.novus.util.aws.secret.SecretsManagerAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;

//@EnableWebSecurity // Not required really
@Configuration
public class JavaSecurityConfiguration {

    // IMPORTANT: Make sure disable the YML Based and also comment SecurityConfiguration.java @config annotation

    private static int USER_CREDENTIAL_RETRY = 2;
    private static JsonNode secretData = null;

    @Autowired

    private ApplicationProperties applicationProperties;

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
    private String retrieveClientId() {
        setSecretData();
        JsonNode tempNode = secretData.get("sso.clientId");
        if (tempNode != null) {
            return tempNode.textValue().trim();
        } else {
            throw new RuntimeException("sso.clientId is not defined in secret");
        }
    }
    private void setSecretData() {
        try {
            if(secretData==null) {
                secretData = retriveSecretValue(applicationProperties.getAwsSecretName(), applicationProperties.getAwsRegion());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to retrieve secret values. Error Message: "+e.getMessage());
        }
    }

    private String retrieveClientSecret() {
        setSecretData();
        JsonNode tempNode = secretData.get("sso.clientSecret");
        if (tempNode != null) {
            return tempNode.textValue().trim();
        } else {
            throw new RuntimeException("sso.clientSecret is not defined in secret");
        }
    }

    private String retrieveRedirectUrl() {
        setSecretData();
        JsonNode tempNode = secretData.get("sso.redirectUrl");
        if (tempNode != null) {
            return tempNode.textValue().trim();
        } else {
            throw new RuntimeException("sso.redirectUrl is not defined in secret");
        }
    }

    // May need to change this code of retrieving the secret data from aws
    private static JsonNode retriveSecretValue(String secretName, String awsRegion) throws Exception {
        JsonNode jsonNode = null;
        int retryAttempt = 1;
        boolean retry = true;
        boolean isFailed = false;
        StringBuilder msg = new StringBuilder(
                "Cannot find the specified secret : " + secretName + " in the awsRegion: " + awsRegion);
        while (retry && (retryAttempt <= USER_CREDENTIAL_RETRY)) {
            try {
                jsonNode = SecretsManagerAccess.getSecretsManagerData(secretName, awsRegion);
                retry = false;
            } catch (Exception e) {
                if (retryAttempt == USER_CREDENTIAL_RETRY) {
                    retry = Boolean.FALSE;
                    isFailed = Boolean.TRUE;
                    if (e.getCause() != null) {
                        msg.append(" | Cause: " + e.getCause());
                    }
                    msg.append(" | Exception msg: " + e.getMessage());
                }
                retryAttempt++;
            }
        }

        if (isFailed) {
            throw new Exception(msg.toString());
        }
        if (jsonNode == null || jsonNode.isEmpty()) {
            throw new RuntimeException("Failed to fetch values from secrets manager. Secret name: " + secretName
                    + " | awsRegion: " + awsRegion);
        }
        return jsonNode;
    }
}
