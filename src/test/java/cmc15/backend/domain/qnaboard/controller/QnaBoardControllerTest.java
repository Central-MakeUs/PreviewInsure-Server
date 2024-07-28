package cmc15.backend.domain.qnaboard.controller;

import cmc15.backend.domain.ControllerTestSupport;
import cmc15.backend.domain.qnaboard.dto.request.QnaBoardRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class QnaBoardControllerTest extends ControllerTestSupport {

    @DisplayName("질문 등록 API")
    @Test
    void 질문_등록_API() throws Exception {
        // given
        QnaBoardRequest.Input request = new QnaBoardRequest.Input("질문 입니다.", true, "하나손해보험");

        // when // then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/quesion")
                        .header("Authorization", "Bearer AccessToken")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}