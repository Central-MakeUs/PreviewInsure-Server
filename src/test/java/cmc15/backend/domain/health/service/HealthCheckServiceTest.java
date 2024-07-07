package cmc15.backend.domain.health.service;

import cmc15.backend.domain.health.dto.request.HealthCheckRequest;
import cmc15.backend.domain.health.dto.response.HealthCheckResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class HealthCheckServiceTest {

    @Autowired
    private HealthCheckService healthCheckService;

    @Test
    @DisplayName("헬스체크 테스트")
    void 헬스체크_테스트() {
        // given

        // when
        HealthCheckResponse.Success healthCheck = healthCheckService.isHealthCheck();

        // then
        assertThat(healthCheck.getIsHealthCheck()).isTrue();
    }

    @Test
    @DisplayName("헬스체크 POST 테스트")
    void 헬스체크_POST_테스트() {
        // given
        HealthCheckRequest.Request request = new HealthCheckRequest.Request("테스트 유저", "안녕하세요");

        // when
        HealthCheckResponse.RequestSuccess response = healthCheckService.isRequestHealthCheck(request);

        // then
        assertThat(response)
                .extracting("name", "input")
                .contains("테스트 유저", "안녕하세요");
    }

    @Test
    @DisplayName("헬스체크 POST 예외발생 테스트")
    void 헬스체크_POST_테스트_예외발생() {
        // given
        HealthCheckRequest.Request request = new HealthCheckRequest.Request("테스트 유저", "ERROR");

        // when // then
        assertThatThrownBy(() -> healthCheckService.isRequestHealthCheck(request))
                .extracting("result.code","result.message")
                .contains(-1, "실패");
    }

}