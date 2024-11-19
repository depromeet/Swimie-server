package com.depromeet.greeting.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.depromeet.config.ControllerTestConfig;
import com.depromeet.config.mock.WithCustomMockMember;
import com.depromeet.greeting.facade.GreetingFacade;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(GreetingController.class)
public class GreetingControllerTest extends ControllerTestConfig {
    @MockBean GreetingFacade greetingFacade;

    @Test
    @WithCustomMockMember
    public void 인삿말을_조회합니다() throws Exception {
        mockMvc.perform(get("/greeting"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("GREETING_1"))
                .andExpect(jsonPath("$.message").value("인삿말 조회에 성공하였습니다"));
    }
}
