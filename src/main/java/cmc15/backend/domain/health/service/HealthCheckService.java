package cmc15.backend.domain.health.service;

import cmc15.backend.domain.health.dto.request.HealthCheckRequest;
import cmc15.backend.domain.health.dto.response.HealthCheckResponse;
import cmc15.backend.global.Result;
import cmc15.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HealthCheckService {

    public HealthCheckResponse.Success isHealthCheck() {
        return HealthCheckResponse.Success.to(true);
    }

    @Transactional
    public HealthCheckResponse.RequestSuccess isRequestHealthCheck(final HealthCheckRequest.Request request) {
        if (request.getInput().equals("ERROR")) {
            throw new CustomException(Result.FAIL);
        }
        return HealthCheckResponse.RequestSuccess.to(request.getName(), request.getInput());
    }
}
