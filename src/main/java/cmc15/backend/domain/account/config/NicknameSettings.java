package cmc15.backend.domain.account.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "nickname")
@Getter
@Setter
public class NicknameSettings {
    private String word1;
    private String word2;

    public String[] getLastName() {
        return word1.split(",");
    }

    public String[] getFirstName() {
        return word2.split(",");
    }
}
