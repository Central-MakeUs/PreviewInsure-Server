package cmc15.backend.domain.health.controller;

import cmc15.backend.domain.ControllerTestSupport;
import cmc15.backend.domain.health.dto.request.HealthCheckRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HealthCheckControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("헬스체크 API")
    void 헬스체크_API_테스트() throws Exception {
        // given

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/health"))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("헬스체크 POST API")
    void 헬스체크_POST_API_테스트() throws Exception {
        // given
        HealthCheckRequest.Request request = new HealthCheckRequest.Request("테스트 유저", "안녕하세요");

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/health")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("헬스체크 POST API")
    void 헬스체크_POST_API_EMPTY_테스트() throws Exception {
        // given
        HealthCheckRequest.Request request = new HealthCheckRequest.Request("", "안녕하세요");

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/health")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}