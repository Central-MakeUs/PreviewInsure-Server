package cmc15.backend.docs;

import cmc15.backend.RestDocsSupport;
import cmc15.backend.domain.account.controller.AccountController;
import cmc15.backend.domain.account.request.AccountRequest;
import cmc15.backend.domain.account.response.AccountResponse;
import cmc15.backend.domain.account.service.AccountService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerDocsTest extends RestDocsSupport {

    AccountService accountService = mock(AccountService.class);

    @Override
    protected Object initController() {
        return new AccountController(accountService);
    }

    @DisplayName("소셜로그인 API")
    @Test
    void 소셜로그인_API() throws Exception {
        // given
        given(accountService.socialLogin(any(), anyString()))
                .willReturn(AccountResponse.OAuthConnection.builder()
                        .accountId(1L)
                        .atk("ATK")
                        .rtk("RTK")
                        .nickname("불편한 코끼리")
                        .build());

        ResourceSnippetParameters resource = ResourceSnippetParameters.builder()
                .tag("계정")
                .summary("소셜로그인 API")
                .description("소셜로그인을 진행하는 API 입니다. 해당 API는 현재 '구글' 만 적용됩니다. \n" +
                        "platform : GOOGLE(지원중)/NAVER(미지원)/KAKAO(미지원) \n")
                .queryParameters(
                        parameterWithName("platform").description("소셜로그인을 지원하는 플랫폼 이름"),
                        parameterWithName("code").description("인가코드"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"),
                        fieldWithPath("data.accountId").type(NUMBER).description("계정 ID"),
                        fieldWithPath("data.atk").type(STRING).description("발급된 accessToken"),
                        fieldWithPath("data.rtk").type(STRING).description("발급된 refreshToken"),
                        fieldWithPath("data.nickname").type(STRING).description("별명"),
                        fieldWithPath("data.redirectUrl").type(STRING).description("애플 로그인으로 인한 URL/ 클라이언트는 필요치 않음").optional())
                .build();

        RestDocumentationResultHandler document = documentHandler("social-login", prettyPrint(), resource);

        // when // then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/oauth")
                        .param("platform", "GOOGLE")
                        .param("code", "2$AS*cvyAS*%jkshvasjhaj4h2S&Abassjasgdashjashj"))
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

    @DisplayName("닉네임 업데이트 API")
    @Test
    void 닉네임_업데이트_API() throws Exception {
        // given
        AccountRequest.Nickname request = new AccountRequest.Nickname("불편한 코끼리");

        ResourceSnippetParameters resource = ResourceSnippetParameters.builder()
                .tag("계정")
                .summary("닉네임 업데이트 API")
                .description("")
                .requestHeaders(headerWithName("Authorization")
                        .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 또는 Authorize 버튼을 이용해 토큰을 넣어주세요"))
                .requestFields(
                        fieldWithPath("nickname").type(STRING).description("변경할 닉네임"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메시지"))
                .build();

        RestDocumentationResultHandler document = documentHandler("update-nickname", prettyPrint(), prettyPrint(), resource);

        // when // then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/register/nickname")
                        .header("Authorization", "Bearer AccessToken")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }

    @DisplayName("탈퇴하기 API")
    @Test
    void 회원탈퇴_API() throws Exception {
        // given
        ResourceSnippetParameters resource = ResourceSnippetParameters.builder()
                .tag("계정")
                .summary("회원탈퇴 API")
                .description("탈퇴하기 API")
                .requestHeaders(headerWithName("Authorization")
                        .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 또는 Authorize 버튼을 이용해 토큰을 넣어주세요"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메시지"))
                .build();

        RestDocumentationResultHandler document = documentHandler("delete-account", prettyPrint(), prettyPrint(), resource);

        // when // then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/account")
                        .header("Authorization", "Bearer AccessToken"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }

    @DisplayName("내 정보 조회 API")
    @Test
    void 내_정보_조회_API() throws Exception {
        // given
        given(accountService.readAccount(any()))
                .willReturn(AccountResponse.Detail.builder()
                        .accountId(1L)
                        .email("previewInsure@gmail.com")
                        .age("25")
                        .gender("선택안")
                        .build());

        ResourceSnippetParameters resource = ResourceSnippetParameters.builder()
                .tag("계정")
                .summary("내 정보 조회 API")
                .description("내 정보 조회 API")
                .requestHeaders(headerWithName("Authorization")
                        .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 또는 Authorize 버튼을 이용해 토큰을 넣어주세요"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메시지"),
                        fieldWithPath("data.accountId").type(NUMBER).description("계정 ID"),
                        fieldWithPath("data.email").type(STRING).description("계정 이메일"),
                        fieldWithPath("data.age").type(STRING).description("나이 / null인 경우 '-'"),
                        fieldWithPath("data.gender").type(STRING).description("성별 / null인 경우 '선택안함'"))
                .build();

        RestDocumentationResultHandler document = documentHandler("read-account", prettyPrint(), prettyPrint(), resource);

        // when // then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/account")
                        .header("Authorization", "Bearer AccessToken"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }
}
