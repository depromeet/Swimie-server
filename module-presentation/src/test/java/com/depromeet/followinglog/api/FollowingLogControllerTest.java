package com.depromeet.followinglog.api;

import static com.depromeet.type.followingLog.FollowingLogSuccessType.GET_FOLLOWING_LOGS_SUCCESS;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.depromeet.config.ControllerTestConfig;
import com.depromeet.config.mock.WithCustomMockMember;
import com.depromeet.followinglog.dto.response.FollowingLogSliceResponse;
import com.depromeet.followinglog.facade.FollowingLogFacade;
import com.depromeet.followinglog.fixture.response.FollowingLogSliceResponseFixture;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(FollowingLogController.class)
public class FollowingLogControllerTest extends ControllerTestConfig {
    @MockBean FollowingLogFacade followingLogFacade;

    @Test
    @WithCustomMockMember
    public void 팔로우_소식을_조회합니다() throws Exception {
        long cursorId = 11L;
        FollowingLogSliceResponse response = FollowingLogSliceResponseFixture.make();

        when(followingLogFacade.getLogsByMemberIdAndCursorId(anyLong(), anyLong()))
                .thenReturn(response);

        mockMvc.perform(get("/news").param("cursorId", Long.toString(cursorId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(GET_FOLLOWING_LOGS_SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(GET_FOLLOWING_LOGS_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.pageSize").value(10))
                .andExpect(jsonPath("$.data.cursorId").value(11))
                .andExpect(jsonPath("$.data.hasNext").value(true));
    }
}
