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

public class AccountInsuranceControllerTest extends ControllerTestSupport {

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

    @DisplayName("내가 가입한 보험 리스트 조회 API")
    @Test
    void 내가_가입한_보험_리스트_조회_API() throws Exception {
        // given

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/account/insurances")
                        .header("Authorization", "Bearer AccessToken"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("내가 가입한 보험 취소 API")
    @Test
    void 내가_가입한_보험_취소_API() throws Exception {
        // given
        AccountRequest.DeleteInsurance request = new AccountRequest.DeleteInsurance(1L);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/account/insurance")
                .header("Authorization", "Bearer AccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON));
    }

    @DisplayName("내가 가입한 보험 수정 API")
    @Test
    void 내가_가입한_보험_수정_API() throws Exception {
        // given
        AccountRequest.UpdateAccountInsurance request = new AccountRequest.UpdateAccountInsurance(1L, InsuranceType.LF, "NH농협생명");

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/account/insurance")
                .header("Authorization", "Bearer AccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON));
    }
}
