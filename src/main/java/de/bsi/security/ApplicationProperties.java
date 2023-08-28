package de.bsi.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class ApplicationProperties {

    @Value("${novus.engine.id}")
    private String novusEngineId;

    @Value("${log.filename}")
    private String logPath;

    @Value("${system.environment}")
    private String systemEnvironment;

    @Value("${user.credentials.filename}")
    private String credentialsFilePath;

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.secretName}")
    private String awsSecretName;
}
