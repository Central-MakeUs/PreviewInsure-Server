package cmc15.backend.domain.qnaboard.controller;

import cmc15.backend.domain.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class QnaBoardControllerTest extends ControllerTestSupport {

    @DisplayName("질문 등록 API")
    @Test
    void 질문_등록_API() throws Exception {
        // given
        // when // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/quesion")
                .header("Authorization", "Bearer AccessToken")
                .param("message","보험을 알아보기 쉬운 애플리케이션은 뭘까?"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}