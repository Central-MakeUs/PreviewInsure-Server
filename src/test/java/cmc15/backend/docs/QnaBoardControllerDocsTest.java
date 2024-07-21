package cmc15.backend.docs;

import cmc15.backend.RestDocsSupport;
import cmc15.backend.domain.qnaboard.controller.QnaBoardController;
import cmc15.backend.domain.qnaboard.dto.response.QnaBoardResponse;
import cmc15.backend.domain.qnaboard.service.QnaBoardService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QnaBoardControllerDocsTest extends RestDocsSupport {
    private final QnaBoardService qnaBoardService = mock(QnaBoardService.class);

    @Override
    protected Object initController() {
        return new QnaBoardController(qnaBoardService);
    }

    @DisplayName("질문 등록 API")
    @Test
    void 질문_등록_API() throws Exception {
        // given
        given(qnaBoardService.inputQuesion(anyString(), any()))
                .willReturn(
                        QnaBoardResponse.Input.builder()
                                .qnaBoardId(1L)
                                .quesion("보험을 알아보기 쉬운 애플리케이션은 뭘까?")
                                .answer("보험을 알아보기 쉬운 애플리케이션을 찾고 계시는 군요! ...")
                                .build()
                );

        ResourceSnippetParameters resources = ResourceSnippetParameters.builder()
                .tag("질문 게시판/AI")
                .summary("질문 등록(AI) API")
                .requestHeaders(headerWithName("Authorization")
                        .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 또는 Authorize 버튼을 이용해 토큰을 넣어주세요"))
                .queryParameters(parameterWithName("message").description("명령 메시지"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"),
                        fieldWithPath("data.qnaBoardId").type(NUMBER).description("질문 Id"),
                        fieldWithPath("data.quesion").type(STRING).description("질문"),
                        fieldWithPath("data.answer").type(STRING).description("AI 대답"))
                .build();

        RestDocumentationResultHandler document = documentHandler("add-quesion", prettyPrint(), resources);

        // when // then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/quesion")
                        .header("Authorization", "Bearer AccessToken")
                        .param("message", "보험을 알아보기 쉬운 애플리케이션은 뭘까?"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }
}
