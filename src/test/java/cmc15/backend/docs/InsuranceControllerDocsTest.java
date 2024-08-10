package cmc15.backend.docs;

import cmc15.backend.RestDocsSupport;
import cmc15.backend.domain.insurance.controller.InsuranceController;
import cmc15.backend.domain.insurance.response.InsuranceResponse;
import cmc15.backend.domain.insurance.service.InsuranceService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InsuranceControllerDocsTest extends RestDocsSupport {

    private final InsuranceService insuranceService = mock(InsuranceService.class);

    @Override
    protected Object initController() {
        return new InsuranceController(insuranceService);
    }

    @DisplayName("보험 타입 추천 API")
    @Test
    void 보험_타입_추천_API() throws Exception {
        // given
        List<InsuranceResponse.Recommend> insuranceRecommends = List.of(
                new InsuranceResponse.Recommend(61L, "이미지URL", "회사 III", "종신 보험 보장 내용", 0.65, 65500, "http://link61.com"),
                new InsuranceResponse.Recommend(1L, "이미지URL", "회사 A", "종신 보험 보장 내용", 0.05, 5500, "http://link1.com"),
                new InsuranceResponse.Recommend(91L, "이미지URL", "회사 MMMM", "종신 보험 보장 내용", 0.95, 95500, "http://link91.com"),
                new InsuranceResponse.Recommend(71L, "이미지URL", "회사 SSS", "종신 보험 보장 내용", 0.75, 75500, "http://link71.com")
        );

        given(insuranceService.insuranceRecommend(any(), any()))
                .willReturn(
                        InsuranceResponse.MapDetail.builder()
                                .nickName("김덕배")
                                .isSubscribed(true)
                                .insuranceCompany("한화생명")
                                .insuranceLink("https://www.hanwhalife.com/index.jsp")
                                .insuranceRecommends(insuranceRecommends)
                                .build());

        ResourceSnippetParameters resource = ResourceSnippetParameters.builder()
                .tag("보험")
                .summary("보험 타입 추천 API")
                .description("해당 유형의 보험이 가입 되어있지 않다면 insuranceCompany와 insuranceLink 필드는 응답값에 제공되지 않습니다. (null)")
                .requestHeaders(headerWithName("Authorization")
                        .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 또는 Authorize 버튼을 이용해 토큰을 넣어주세요"))
                .responseFields(
                        fieldWithPath("code").description("상태 코드"),
                        fieldWithPath("message").description("상태 메시지"),
                        fieldWithPath("data.nickName").description("유저 닉네임"),
                        fieldWithPath("data.isSubscribed").description("해당 보험 가입 여부"),
                        fieldWithPath("data.insuranceCompany").description("가입된 보험 회사 / 가입 여부 false인 경우 null"),
                        fieldWithPath("data.insuranceLink").description("가입된 보험 회사 링크 / 가입 여부 false인 경우 null"),
                        fieldWithPath("data.insuranceRecommends[].insuranceId").description("보험 Id"),
                        fieldWithPath("data.insuranceRecommends[].insuranceImage").description("보험 회사 이미지"),
                        fieldWithPath("data.insuranceRecommends[].insuranceCompany").description("보험 회사 이름"),
                        fieldWithPath("data.insuranceRecommends[].insuranceContent").description("보험명"),
                        fieldWithPath("data.insuranceRecommends[].insuranceRate").description("보험 가격 지수"),
                        fieldWithPath("data.insuranceRecommends[].price").description("추정 평균 가입 금액"),
                        fieldWithPath("data.insuranceRecommends[].link").description("보험 상품 링크")
                )
                .queryParameters(
                        parameterWithName("insuranceType").description("보험 유형")
                )
                .build();

        RestDocumentationResultHandler document = documentHandler("insurance-recommend", prettyPrint(), resource);

        // when // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/recommend")
                                .header("Authorization", "Bearer AccessToken")
                                .param("insuranceType", "LF"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }
}