package com.depromeet.notification.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.depromeet.config.ControllerTestConfig;
import com.depromeet.config.mock.WithCustomMockMember;
import com.depromeet.notification.facade.NotificationFacade;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest extends ControllerTestConfig {
    @MockBean NotificationFacade notificationFacade;

    @Test
    @WithCustomMockMember
    public void 알림을_조회합니다() throws Exception {
        mockMvc.perform(get("/notification"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("NOTIFICATION_1"))
                .andExpect(jsonPath("$.message").value("알림 조회에 성공하였습니다"))
                .andDo(print());
    }

    @Test
    @WithCustomMockMember
    public void 알림을_확인처리합니다() throws Exception {
        mockMvc.perform(patch("/notification/read"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("NOTIFICATION_2"))
                .andExpect(jsonPath("$.message").value("알림을 읽음 처리하였습니다"))
                .andDo(print());
    }

    @Test
    @WithCustomMockMember
    public void 미확인_알림_개수를_조회합니다() throws Exception {
        mockMvc.perform(get("/notification/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("NOTIFICATION_3"))
                .andExpect(jsonPath("$.message").value("읽지 않은 알림 개수 조회에 성공하였습니다"))
                .andDo(print());
    }
}
