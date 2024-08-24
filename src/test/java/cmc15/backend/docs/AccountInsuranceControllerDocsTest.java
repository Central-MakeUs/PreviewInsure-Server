package cmc15.backend.docs;

import cmc15.backend.RestDocsSupport;
import cmc15.backend.domain.account.controller.AccountInsuranceController;
import cmc15.backend.domain.account.entity.InsuranceType;
import cmc15.backend.domain.account.request.AccountRequest;
import cmc15.backend.domain.account.response.AccountResponse;
import cmc15.backend.domain.account.service.AccountInsuranceService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountInsuranceControllerDocsTest extends RestDocsSupport {


    AccountInsuranceService accountInsuranceService = mock(AccountInsuranceService.class);

    @Override
    protected Object initController() {
        return new AccountInsuranceController(accountInsuranceService);
    }

    @DisplayName("내가 가입한 보험 리스트 조회 API")
    @Test
    void 내가_가입한_보험_리스트_조회_API() throws Exception {
        // given
        given(accountInsuranceService.readAccountInsurances(any()))
                .willReturn(List.of(
                        new AccountResponse.Insurances(1L, InsuranceType.CI, "하나손해보험"),
                        new AccountResponse.Insurances(2L, InsuranceType.SI, "하나손해보험"),
                        new AccountResponse.Insurances(3L, InsuranceType.PA, "하나손해보험"),
                        new AccountResponse.Insurances(4L, InsuranceType.ED, "하나손해보험")
                ));

        ResourceSnippetParameters resource = ResourceSnippetParameters.builder()
                .tag("계정")
                .summary("내가 가입한 보험 리스트 조회 API")
                .description("")
                .requestHeaders(headerWithName("Authorization")
                        .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 또는 Authorize 버튼을 이용해 토큰을 넣어주세요"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메시지"),
                        fieldWithPath("data[].accountInsuranceId").type(NUMBER).description("내 보험 ID"),
                        fieldWithPath("data[].insuranceType").type(STRING).description("내 보험 유형"),
                        fieldWithPath("data[].insuranceCompany").type(STRING).description("내 보험 회사"))
                .build();

        RestDocumentationResultHandler document = documentHandler("read-account-insurances", prettyPrint(), resource);

        // when // then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/account/insurances")
                        .header("Authorization", "Bearer AccessToken"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }

    @DisplayName("인슈보딩 입력 API")
    @Test
    void 인슈보딩_입력_API() throws Exception {
        // given
        AccountRequest.InsureBoarding.InsureBoard insureBoard1 = new AccountRequest.InsureBoarding.InsureBoard(
                InsuranceType.CI, "하나손해보험"
        );

        AccountRequest.InsureBoarding.InsureBoard insureBoard2 = new AccountRequest.InsureBoarding.InsureBoard(
                InsuranceType.DR, "메리츠 화재"
        );

        AccountRequest.InsureBoarding.InsureBoard insureBoard3 = new AccountRequest.InsureBoarding.InsureBoard(
                InsuranceType.ED, "하나손해보험"
        );

        AccountRequest.InsureBoarding request = new AccountRequest.InsureBoarding("M", List.of(insureBoard1, insureBoard2, insureBoard3));

        ResourceSnippetParameters resource = ResourceSnippetParameters.builder()
                .tag("계정")
                .summary("인슈보딩 입력 API")
                .requestFields(
                        fieldWithPath("gender").type(STRING).description("M : 남자 / W : 여자").optional(),
                        fieldWithPath("insureBoards[]").type(ARRAY).description("보험 유형 및 회사 리스트").optional(),
                        fieldWithPath("insureBoards[].insuranceType").type(STRING).description("기능/상태코드 문서 참고"),
                        fieldWithPath("insureBoards[].insuranceCompany").type(STRING).description("기능/상태코드 문서 참고"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"))
                .build();

        RestDocumentationResultHandler document = documentHandler("insure_boarding_add", prettyPrint(), prettyPrint(), resource);

        // when // then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/register/board")
                        .header("Authorization", "Bearer AccessToken")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }

    @DisplayName("내가 가입한 보험 취소 API")
    @Test
    void 내가_가입한_보험_취소_API() throws Exception {
        // given
        AccountRequest.DeleteInsurance request = new AccountRequest.DeleteInsurance(1L);

        ResourceSnippetParameters resource = ResourceSnippetParameters.builder()
                .tag("계정")
                .summary("내가 가입한 보험 취소 API")
                .requestFields(
                        fieldWithPath("accountInsuranceId").type(NUMBER).description("내가 가입한 보험 Id"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"))
                .build();

        RestDocumentationResultHandler document = documentHandler("deltete-account-insurance", prettyPrint(), prettyPrint(), resource);

        // when // then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/account/insurance")
                .header("Authorization", "Bearer AccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON))
                .andDo(document);
    }
}
