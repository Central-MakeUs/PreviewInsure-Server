package cmc15.backend.domain.health.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class HealthCheckRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Request {
        @NotBlank(message = "내용이 비어있습니다.")
        private String name;
        private String input;
    }
}
