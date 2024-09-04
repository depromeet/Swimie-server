package com.depromeet.withdrawal.api;

import static com.depromeet.type.withdrawal.WithdrawalReasonSuccessType.POST_WITHDRAWAL_REASON_SUCCESS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.depromeet.config.ControllerTestConfig;
import com.depromeet.config.mock.WithCustomMockMember;
import com.depromeet.withdrawal.dto.request.WithdrawalReasonCreateRequest;
import com.depromeet.withdrawal.facade.WithdrawalReasonFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(controllers = WithdrawalReasonController.class)
public class WithdrawalReasonControllerTest extends ControllerTestConfig {
    @Autowired private ObjectMapper objectMapper;

    @MockBean WithdrawalReasonFacade withdrawalReasonFacade;

    @Test
    @WithCustomMockMember
    void 탈퇴_사유_등록() throws Exception {
        // given
        WithdrawalReasonCreateRequest request =
                new WithdrawalReasonCreateRequest("REASON_01", "test feedback");
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("request", request);

        // when
        doNothing().when(withdrawalReasonFacade).save(any(WithdrawalReasonCreateRequest.class));

        // then
        mockMvc.perform(
                        post("/withdrawal")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(POST_WITHDRAWAL_REASON_SUCCESS.getCode()))
                .andExpect(
                        jsonPath("$.message").value(POST_WITHDRAWAL_REASON_SUCCESS.getMessage()));
    }
}
