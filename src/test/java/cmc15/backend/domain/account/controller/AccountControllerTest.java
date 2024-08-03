package cmc15.backend.domain.account.controller;

import cmc15.backend.domain.ControllerTestSupport;
import cmc15.backend.domain.account.entity.InsuranceType;
import cmc15.backend.domain.account.request.AccountRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountControllerTest extends ControllerTestSupport {

    @DisplayName("회원가입 API")
    @Test
    void 회원가입_API() throws Exception {
        // given
        AccountRequest.Register request = new AccountRequest.Register(
                "김덕배", "hwsa10041@gmail.com", "abc123"
        );

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("랜덤 닉네임 생성 API")
    @Test
    void 랜덤_닉네임_생성_API() throws Exception {
        // given

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/register/nickname"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("나이 입력 API")
    @Test
    void 나이_입력_API() throws Exception {
        // given
        AccountRequest.Age request = new AccountRequest.Age(2000, 10);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/register/age")
                        .header("Authorization", "Bearer AccessToken")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("인슈보딩 입력 API")
    @Test
    void 인슈보딩_입력_API() throws Exception {
        // given
        AccountRequest.InsureBoarding.InsureBoard insureBoard1 = new AccountRequest.InsureBoarding.InsureBoard(
                InsuranceType.CI, "하나손해보험"
        );

        AccountRequest.InsureBoarding.InsureBoard insureBoard2 = new AccountRequest.InsureBoarding.InsureBoard(
                InsuranceType.DR, "메리즈화재"
        );

        AccountRequest.InsureBoarding.InsureBoard insureBoard3 = new AccountRequest.InsureBoarding.InsureBoard(
                InsuranceType.ED, "하나손해보험"
        );

        AccountRequest.InsureBoarding request = new AccountRequest.InsureBoarding("M", List.of(insureBoard1, insureBoard2, insureBoard3));

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/register/board")
                        .header("Authorization", "Bearer AccessToken")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}