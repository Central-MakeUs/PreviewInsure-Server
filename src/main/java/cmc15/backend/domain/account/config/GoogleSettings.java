package cmc15.backend.domain.account.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "google")
@Getter
@Setter
public class GoogleSettings {
    private String clientId;
    private String clientSecret;
    private String redirectUrl;
}
