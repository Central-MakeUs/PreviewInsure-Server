package cmc15.backend.domain.insurance.controller;

import cmc15.backend.domain.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InsuranceControllerTest extends ControllerTestSupport {

    @DisplayName("보험 타입 추천 API")
    @Test
    void 보험_타입_추천_API() throws Exception {
        // given

        // when // then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/recommend")
                                .header("Authorization", "Bearer AccessToken")
                                .param("insuranceType", "LF"))
                .andDo(print())
                .andExpect(status().isOk());
    }

}