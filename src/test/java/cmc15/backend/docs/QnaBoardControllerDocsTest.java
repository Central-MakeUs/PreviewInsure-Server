package cmc15.backend.docs;

import cmc15.backend.RestDocsSupport;
import cmc15.backend.domain.qnaboard.controller.QnaBoardController;
import cmc15.backend.domain.qnaboard.dto.request.QnaBoardRequest;
import cmc15.backend.domain.qnaboard.dto.response.QnaBoardResponse;
import cmc15.backend.domain.qnaboard.service.QnaBoardService;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
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
        QnaBoardRequest.Input request = new QnaBoardRequest.Input("질문 입니다.", true, "하나손해보험");

        given(qnaBoardService.inputQuesion(any(), any(QnaBoardRequest.Input.class)))
                .willReturn(QnaBoardResponse.Input.builder()
                        .qnaBoardId(1L)
                        .quesion("보험을 알아보기 쉬운 애플리케이션은 뭘까?")
                        .answer("보험을 알아보기 쉬운 애플리케이션을 찾고 계시는 군요! ...")
                        .isShare(true)
                        .insuranceType("하나손해보험")
                        .build()
                );

        ResourceSnippetParameters resources = ResourceSnippetParameters.builder()
                .tag("질문 게시판/AI")
                .summary("질문 등록(AI) API")
                .requestHeaders(headerWithName("Authorization")
                        .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 또는 Authorize 버튼을 이용해 토큰을 넣어주세요"))
                .requestFields(
                        fieldWithPath("quesion").type(STRING).description("명령 메시지"),
                        fieldWithPath("isShare").type(BOOLEAN).description("전체 게시판 질문 내용 공유 여부 (true:1, false:0"),
                        fieldWithPath("insuranceType").type(STRING).description("보험 유형"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"),
                        fieldWithPath("data.qnaBoardId").type(NUMBER).description("질문 Id"),
                        fieldWithPath("data.quesion").type(STRING).description("질문"),
                        fieldWithPath("data.answer").type(STRING).description("AI 대답"),
                        fieldWithPath("data.isShare").type(BOOLEAN).description("전체 게시판 질문 공유 여부"),
                        fieldWithPath("data.insuranceType").type(STRING).description("보험 유형"))
                .build();

        RestDocumentationResultHandler document = documentHandler("add-quesion", prettyPrint(), resources);

        // when // then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/quesion")
                        .header("Authorization", "Bearer AccessToken")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }

    @DisplayName("내 질문 타이틀 조회 API")
    @Test
    void 내_질문_타이틀_조회_API() throws Exception {
        // given
        given(qnaBoardService.readQuesionTitles(any()))
                .willReturn(
                        List.of(
                                QnaBoardResponse.ReadQuesionTitle.builder().qnaBoardId(1L).title("안녕하세요!").build(),
                                QnaBoardResponse.ReadQuesionTitle.builder().qnaBoardId(2L).title("질문입니다").build(),
                                QnaBoardResponse.ReadQuesionTitle.builder().qnaBoardId(3L).title("배가고파요").build(),
                                QnaBoardResponse.ReadQuesionTitle.builder().qnaBoardId(4L).title("메뉴추천좀해줘").build(),
                                QnaBoardResponse.ReadQuesionTitle.builder().qnaBoardId(5L).title("놀거리추천좀해줘").build())
                );

        ResourceSnippetParameters resources = ResourceSnippetParameters.builder()
                .tag("질문 게시판/AI")
                .summary("내 질문 타이틀 조회 API")
                .description("내 질문들 Id 및 질문 내용 조회")
                .requestHeaders(headerWithName("Authorization")
                        .description("Swagger 요청시 해당 입력칸이 아닌 우측 상단 자물쇠 또는 Authorize 버튼을 이용해 토큰을 넣어주세요"))
                .responseFields(
                        fieldWithPath("code").type(NUMBER).description("상태 코드"),
                        fieldWithPath("message").type(STRING).description("상태 메세지"),
                        fieldWithPath("data[].qnaBoardId").type(NUMBER).description("질문 Id"),
                        fieldWithPath("data[].title").type(STRING).description("질문"))
                .build();

        RestDocumentationResultHandler document = documentHandler("read-quesion-titles", prettyPrint(), resources);

        // when // then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/quesion")
                        .header("Authorization", "Bearer AccessToken"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document);
    }
}
