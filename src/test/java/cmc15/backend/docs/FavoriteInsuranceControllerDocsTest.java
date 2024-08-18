package cmc15.backend.docs;

import cmc15.backend.RestDocsSupport;
import cmc15.backend.domain.account.controller.FavoriteInsuranceController;
import cmc15.backend.domain.account.request.FavoriteInsuranceRequest;
import cmc15.backend.domain.account.response.FavoriteInsuranceResponse;
import cmc15.backend.domain.account.service.FavoriteInsuranceService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import java.util.List;

import static cmc15.backend.domain.account.entity.InsuranceType.LF;
import static cmc15.backend.domain.account.entity.InsuranceType.TD;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FavoriteInsuranceControllerDocsTest extends RestDocsSupport {

    FavoriteInsuranceService favoriteInsuranceService = mock(FavoriteInsuranceService.class);

    @Override
    protected Object initController() {
        return new FavoriteInsuranceController(favoriteInsuranceService);
    }

    @DisplayName("관심보험 등록 API")
    @Test
    void 관심보험_등록_API() throws Exception {
        // given
        FavoriteInsuranceRequest.Add request = new FavoriteInsuranceRequest.Add(LF);

        ResourceSnippetParameters resource = ResourceSnippetParameters.builder()
                .tag("계정")
                .summary("관심보험 등록 API")
                .requestHeaders(headerWithName("Authorization")
                        .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 또는 Authorize 버튼을 이용해 토큰을 넣어주세요"))
                .requestFields(
                        fieldWithPath("insuranceType").type(STRING).description("보험 유형"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"))
                .build();

        RestDocumentationResultHandler document = documentHandler("add-favorite-insurance", prettyPrint(), prettyPrint(), resource);

        // when // then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/account/favorite")
                        .header("Authorization", "Bearer AccessToken")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }

    @DisplayName("내 관심보험 전체 조회 API")
    @Test
    void 내_관심보험_전체_조회_API() throws Exception {
        // given
        given(favoriteInsuranceService.readFavoriteInsurances(any()))
                .willReturn(List.of(
                        FavoriteInsuranceResponse.Detail.builder().favoriteInsuranceId(1L).insuranceType(LF).build(),
                        FavoriteInsuranceResponse.Detail.builder().favoriteInsuranceId(2L).insuranceType(TD).build()
                ));

        ResourceSnippetParameters resource = ResourceSnippetParameters.builder()
                .tag("계정")
                .summary("내 관심보험 전체 조회 API")
                .requestHeaders(headerWithName("Authorization")
                        .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 또는 Authorize 버튼을 이용해 토큰을 넣어주세요"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"),
                        fieldWithPath("data[].favoriteInsuranceId").type(NUMBER).description("내 관심보험 ID"),
                        fieldWithPath("data[].insuranceType").type(STRING).description("보험 타입"))
                .build();

        RestDocumentationResultHandler document = documentHandler("read-favorite-insurance", prettyPrint(), prettyPrint(), resource);

        // when // then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/account/favorite")
                        .header("Authorization", "Bearer AccessToken"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }
}
