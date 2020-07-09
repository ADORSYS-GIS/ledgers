package de.adorsys.ledgers.middleware.impl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ledgers.verify")
public class EmailVerificationProperties {
    private String extBasePath;
    private String endPoint;
    private EmailTemplate template;

    @Data
    public static class EmailTemplate {
        private String subject;
        private String from;
        private String message;
    }
}
