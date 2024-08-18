package cmc15.backend.domain.account.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "apple")
@Getter
@Setter
public class AppleSettings {
    private String authUrl;
    private String teamId;
    private String redirectUrl;
    private String clientId;
    private String keyId;
    private String keyPath;
    private String privateKey;
}
