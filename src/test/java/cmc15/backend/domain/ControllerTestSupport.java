package cmc15.backend.domain;

import cmc15.backend.domain.account.controller.AccountController;
import cmc15.backend.domain.health.controller.HealthCheckController;
import cmc15.backend.domain.qnaboard.controller.QnaBoardController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {HealthCheckController.class, AccountController.class, QnaBoardController.class})
@AutoConfigureMockMvc(addFilters = false)
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected HealthCheckController healthCheckController;

    @MockBean
    protected AccountController accountController;

    @MockBean
    protected  QnaBoardController qnaBoardController;
}
