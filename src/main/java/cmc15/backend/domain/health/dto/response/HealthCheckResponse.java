package cmc15.backend.domain.health.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class HealthCheckResponse {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Success {
        private Boolean isHealthCheck;

        public static Success to(final Boolean isHealthCheck) {
            return Success.builder()
                    .isHealthCheck(isHealthCheck)
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class RequestSuccess {
        private String name;
        private String input;

        public static RequestSuccess to(String name, String input) {
            return RequestSuccess.builder()
                    .name(name)
                    .input(input)
                    .build();
        }
    }
}
