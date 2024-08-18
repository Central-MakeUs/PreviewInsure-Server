package cmc15.backend.domain.account.controller;

import cmc15.backend.domain.ControllerTestSupport;
import cmc15.backend.domain.account.request.FavoriteInsuranceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static cmc15.backend.domain.account.entity.InsuranceType.LF;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FavoriteInsuranceControllerTest extends ControllerTestSupport {

    @DisplayName("관심보험 등록 API")
    @Test
    void 관심보험_등록_API() throws Exception {
        // given
        FavoriteInsuranceRequest.Add request = new FavoriteInsuranceRequest.Add(LF);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account/favorite")
                        .header("Authorization", "Bearer AccessToken")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}