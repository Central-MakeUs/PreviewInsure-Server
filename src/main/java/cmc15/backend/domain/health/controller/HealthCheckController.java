package cmc15.backend.domain.health.controller;

import cmc15.backend.domain.health.dto.request.HealthCheckRequest;
import cmc15.backend.domain.health.dto.response.HealthCheckResponse;
import cmc15.backend.domain.health.service.HealthCheckService;
import cmc15.backend.global.CustomResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HealthCheckController {
    private final HealthCheckService healthCheckService;

    @GetMapping("/health")
    public CustomResponseEntity<HealthCheckResponse.Success> isHealthCheck() {
        return CustomResponseEntity.success(healthCheckService.isHealthCheck());
    }

    @PostMapping("/health")
    public CustomResponseEntity<HealthCheckResponse.RequestSuccess> isRequestHealthCheck(
            @RequestBody @Valid final HealthCheckRequest.Request request
    ) {
        return CustomResponseEntity.success(healthCheckService.isRequestHealthCheck(request));
    }
}
