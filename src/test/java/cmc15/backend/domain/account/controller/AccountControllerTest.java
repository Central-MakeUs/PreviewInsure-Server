package cmc15.backend.domain.account.controller;

import cmc15.backend.domain.ControllerTestSupport;
import cmc15.backend.domain.account.request.AccountRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountControllerTest extends ControllerTestSupport {

    @DisplayName("회원가입 API")
    @Test
    void 회원가입_API() throws Exception {
        // given
        AccountRequest.Register request = new AccountRequest.Register(
                "천현우", "김덕배", "hwsa10041@gmail.com", "abc123"
        );

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}