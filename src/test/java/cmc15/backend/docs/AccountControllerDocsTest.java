package cmc15.backend.docs;

import cmc15.backend.RestDocsSupport;
import cmc15.backend.domain.account.controller.AccountController;
import cmc15.backend.domain.account.entity.InsuranceType;
import cmc15.backend.domain.account.request.AccountRequest;
import cmc15.backend.domain.account.response.AccountResponse;
import cmc15.backend.domain.account.service.AccountService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.epages.restdocs.apispec.Schema.schema;
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

public class AccountControllerDocsTest extends RestDocsSupport {

    AccountService accountService = mock(AccountService.class);

    @Override
    protected Object initController() {
        return new AccountController(accountService);
    }

    @DisplayName("회원가입 API docs 작성")
    @Test
    void 회원가입_API_DOCS() throws Exception {
        // given
        AccountRequest.Register request = new AccountRequest.Register(
                "김덕배", "hwsa10041@gmail.com", "abc123"
        );

        given(accountService.accountRegister(any(AccountRequest.Register.class)))
                .willReturn(AccountResponse.Connection.builder()
                        .accountId(1L)
                        .nickName("김덕배")
                        .email("hwsa10041@gmail.com")
                        .atk("발급된 accessToken")
                        .rtk("발급된 refreshToken")
                        .build());

        ResourceSnippetParameters resource = ResourceSnippetParameters.builder()
                .tag("계정")
                .summary("회원가입 API")
                .description("회원가입을 진행하는 API")
                .requestSchema(schema("AccountRequest.Register"))
                .responseSchema(schema("AccountResponse.Connection"))
                .requestFields(
                        fieldWithPath("nickName").type(STRING).description("별명"),
                        fieldWithPath("email").type(STRING).description("이메일"),
                        fieldWithPath("password").type(STRING).description("비밀번호"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"),
                        fieldWithPath("data.accountId").type(NUMBER).description("계정 ID"),
                        fieldWithPath("data.nickName").type(STRING).description("별명"),
                        fieldWithPath("data.email").type(STRING).description("이메일"),
                        fieldWithPath("data.atk").type(STRING).description("발급된 accessToken"),
                        fieldWithPath("data.rtk").type(STRING).description("발급된 refreshToken"))
                .build();

        RestDocumentationResultHandler document = documentHandler("register-api", prettyPrint(), prettyPrint(), resource);

        // when // then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/account")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }

    @DisplayName("랜덤 닉네임 생성 API")
    @Test
    void 랜덤_닉네임_생성_API() throws Exception {
        // given
        given(accountService.createNickName())
                .willReturn(AccountResponse.NickName.to("랩하는 얼룩말"));


        ResourceSnippetParameters resource = ResourceSnippetParameters.builder()
                .tag("계정")
                .summary("랜덤 닉네임 생성 API")
                .description("랜덤 닉네임을 생성하는 API")
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"),
                        fieldWithPath("data.nickName").type(STRING).description("생성된 닉네임"))
                .build();

        RestDocumentationResultHandler document = documentHandler("create_nickname", prettyPrint(), resource);

        // when // then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/register/nickname"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }

    @DisplayName("나이 입력 API")
    @Test
    void 나이_입력_API() throws Exception {
        // given
        AccountRequest.Age request = new AccountRequest.Age(2000, 10);

        ResourceSnippetParameters resource = ResourceSnippetParameters.builder()
                .tag("계정")
                .summary("나이 입력 API")
                .description("나이 입력 API")
                .requestFields(
                        fieldWithPath("year").type(NUMBER).description("태어난 연도"),
                        fieldWithPath("month").type(NUMBER).description("태어난 달"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"))
                .build();

        RestDocumentationResultHandler document = documentHandler("update_age", prettyPrint(), prettyPrint(), resource);

        // when // then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/register/age")
                        .header("Authorization", "Bearer AccessToken")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
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

    @DisplayName("내가 가입한 보험 리스트 조회 API")
    @Test
    void 내가_가입한_보험_리스트_조회_API() throws Exception {
        // given
        given(accountService.readAccountInsurances(any()))
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

        documentHandler("read-account-insurances", prettyPrint(), resource);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/account/insurances")
                        .header("Authorization", "Bearer AccessToken"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
