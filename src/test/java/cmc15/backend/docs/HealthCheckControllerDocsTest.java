package cmc15.backend.docs;

import cmc15.backend.RestDocsSupport;
import cmc15.backend.domain.health.controller.HealthCheckController;
import cmc15.backend.domain.health.dto.request.HealthCheckRequest;
import cmc15.backend.domain.health.dto.response.HealthCheckResponse;
import cmc15.backend.domain.health.service.HealthCheckService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HealthCheckControllerDocsTest extends RestDocsSupport {
    private final HealthCheckService healthCheckService = mock(HealthCheckService.class);

    @Override
    protected Object initController() {
        return new HealthCheckController(healthCheckService);
    }

    @Test
    @DisplayName("헬스체크 API 문서 테스트")
    void 헬스체크_API() throws Exception {
        // given
        given(healthCheckService.isHealthCheck())
                .willReturn(HealthCheckResponse.Success.builder()
                        .isHealthCheck(true)
                        .build());

        ResourceSnippetParameters resource = ResourceSnippetParameters.builder()
                .tag("헬스체크 API")
                .summary("서버 헬스체크 API")
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"),
                        fieldWithPath("data.isHealthCheck").type(BOOLEAN).description("성공값"))
                .build();

        RestDocumentationResultHandler document = documentHandler("healthCheck-api", prettyPrint(), resource);

        // when // then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/health"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }

    @Test
    @DisplayName("헬스체크 POST API")
    void 헬스체크_POST_API_테스트() throws Exception {
        // given
        HealthCheckRequest.Request request = new HealthCheckRequest.Request("테스트 유저", "안녕하세요");

        given(healthCheckService.isRequestHealthCheck(any(HealthCheckRequest.Request.class)))
                .willReturn(HealthCheckResponse.RequestSuccess.builder()
                        .name("김형만")
                        .input("짱구야!")
                        .build());

        ResourceSnippetParameters resource = ResourceSnippetParameters.builder()
                .tag("헬스체크 API")
                .summary("헬스체크 POST API")
                .requestFields(
                        fieldWithPath("name").type(STRING).description("이름"),
                        fieldWithPath("input").type(STRING).description("테스트 내용"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"),
                        fieldWithPath("data.name").type(STRING).description("이름"),
                        fieldWithPath("data.input").type(STRING).description("테스트 내용"))
                .build();

        RestDocumentationResultHandler document = documentHandler("healthCheck-post-api", prettyPrint(), prettyPrint(), resource);

        // when // then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/health")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }
}