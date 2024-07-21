package cmc15.backend.docs;

import cmc15.backend.RestDocsSupport;
import cmc15.backend.domain.account.controller.AccountController;
import cmc15.backend.domain.account.request.AccountRequest;
import cmc15.backend.domain.account.response.AccountResponse;
import cmc15.backend.domain.account.service.AccountService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static com.epages.restdocs.apispec.Schema.schema;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
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
                "천현우", "김덕배", "hwsa10041@gmail.com", "abc123"
        );

        given(accountService.accountRegister(any(AccountRequest.Register.class)))
                .willReturn(AccountResponse.Connection.builder()
                        .accountId(1L)
                        .name("천현우")
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
                        fieldWithPath("name").type(STRING).description("실제 이름"),
                        fieldWithPath("nickName").type(STRING).description("별명"),
                        fieldWithPath("email").type(STRING).description("이메일"),
                        fieldWithPath("password").type(STRING).description("비밀번호"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"),
                        fieldWithPath("data.accountId").type(NUMBER).description("계정 ID"),
                        fieldWithPath("data.name").type(STRING).description("실제 이름"),
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
}
