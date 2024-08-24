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

    @DisplayName("내 관심보험 전체 조회 API")
    @Test
    void 내_관심보험_전체_조회_API() throws Exception {
        // given

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/account/favorite")
                        .header("Authorization", "Bearer AccessToken"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("내 관심보험 취소 API")
    @Test
    void 내_관심보험_취소_API() throws Exception {
        // given
        FavoriteInsuranceRequest.Delete request = new FavoriteInsuranceRequest.Delete(1L);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/account/favorite")
                        .header("Authorization", "Bearer AccessToken")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}