package com.depromeet.friend.api;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.depromeet.config.ControllerTestConfig;
import com.depromeet.config.mock.WithCustomMockMember;
import com.depromeet.friend.dto.request.FollowCheckResponse;
import com.depromeet.friend.dto.request.FollowRequest;
import com.depromeet.friend.dto.response.*;
import com.depromeet.friend.facade.FollowFacade;
import com.depromeet.friend.fixture.response.FollowerResponseFixture;
import com.depromeet.friend.fixture.response.FollowingResponseFixture;
import com.depromeet.type.friend.FollowSuccessType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(controllers = FollowController.class)
public class FollowControllerTest extends ControllerTestConfig {
    @Autowired private ObjectMapper objectMapper;

    @MockBean private FollowFacade followFacade;

    @Test
    @WithCustomMockMember
    public void 팔로잉_추가() throws Exception {
        // given
        Map<String, Object> requestBody = new HashMap<>();

        FollowRequest request = new FollowRequest(2L);
        requestBody.put("followRequest", request);

        // when
        when(followFacade.addOrDeleteFollow(anyLong(), any(FollowRequest.class))).thenReturn(true);

        // then
        mockMvc.perform(
                        put("/friend")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("FOLLOW_1"))
                .andExpect(jsonPath("$.message").value("팔로잉 추가에 성공하였습니다"));
    }

    @Test
    @WithCustomMockMember
    public void 팔로잉_삭제() throws Exception {
        // given
        Map<String, Object> requestBody = new HashMap<>();

        FollowRequest request = new FollowRequest(2L);
        requestBody.put("followRequest", request);

        // when
        when(followFacade.addOrDeleteFollow(anyLong(), any(FollowRequest.class))).thenReturn(false);

        // then
        mockMvc.perform(
                        put("/friend")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("FOLLOW_2"))
                .andExpect(jsonPath("$.message").value("팔로잉 삭제에 성공하였습니다"));
    }

    @Test
    @WithCustomMockMember
    void 팔로잉목록_조회() throws Exception {
        // given
        Long memberId = 1L;
        Long cursorId = 10L;

        List<FollowingResponse> followingResponses = FollowingResponseFixture.make();

        FollowSliceResponse<FollowingResponse> response =
                new FollowSliceResponse<>(
                        followingResponses, followingResponses.size(), null, false);

        // when
        when(followFacade.findFollowingList(anyLong(), anyLong(), anyLong())).thenReturn(response);

        // then
        mockMvc.perform(
                        get("/friend/{memberId}/following", memberId)
                                .param("cursorId", cursorId.toString()))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.code")
                                .value(FollowSuccessType.GET_FOLLOWINGS_SUCCESS.getCode()))
                .andExpect(
                        jsonPath("$.message")
                                .value(FollowSuccessType.GET_FOLLOWINGS_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data.contents").isArray())
                .andExpect(jsonPath("$.data.pageSize").value(10))
                .andExpect(jsonPath("$.data.hasNext").value(false));
    }

    @Test
    @WithCustomMockMember
    void 팔로워목록_조회() throws Exception {
        // given
        Long memberId = 1L;
        Long cursorId = 10L;

        List<FollowerResponse> followerResponses = FollowerResponseFixture.make();

        FollowSliceResponse<FollowerResponse> response =
                new FollowSliceResponse<>(followerResponses, followerResponses.size(), null, false);

        // when
        when(followFacade.findFollowerList(anyLong(), anyLong(), anyLong())).thenReturn(response);

        // then
        mockMvc.perform(
                        get("/friend/{memberId}/follower", memberId)
                                .param("cursorId", cursorId.toString()))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.code").value(FollowSuccessType.GET_FOLLOWERS_SUCCESS.getCode()))
                .andExpect(
                        jsonPath("$.message")
                                .value(FollowSuccessType.GET_FOLLOWERS_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data.contents").isArray())
                .andExpect(jsonPath("$.data.pageSize").value(10))
                .andExpect(jsonPath("$.data.hasNext").value(false));
    }

    @Test
    @WithCustomMockMember
    void 팔로잉_요약_조회() throws Exception {
        // given
        List<FollowingResponse> followingResponses = FollowingResponseFixture.make().subList(0, 3);
        int followingCount = 10;

        FollowingSummaryResponse response =
                FollowingSummaryResponse.builder()
                        .followings(followingResponses)
                        .followingCount(followingCount)
                        .build();

        // when
        when(followFacade.findFollowingSummary(anyLong())).thenReturn(response);

        // then
        mockMvc.perform(get("/friend/following/summary"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.code")
                                .value(FollowSuccessType.GET_FOLLOWING_SUMMARY_SUCCESS.getCode()))
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        FollowSuccessType.GET_FOLLOWING_SUMMARY_SUCCESS
                                                .getMessage()))
                .andExpect(jsonPath("$.data.followings").isArray())
                .andExpect(jsonPath("$.data.followingCount").value(10));
    }

    @Test
    @WithCustomMockMember
    void 팔로잉_여부_체크() throws Exception {
        List<FollowCheckResponse> followCheckResponses = new ArrayList<>();
        List<Long> followingIds = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            long followingId = i + 2;
            FollowCheckResponse followCheckResponse =
                    new FollowCheckResponse(followingId, i % 2 == 0);
            followCheckResponses.add(followCheckResponse);
            followingIds.add(followingId);
        }
        FollowingStateResponse response = new FollowingStateResponse(followCheckResponses);

        when(followFacade.checkFollowingState(anyLong(), anyList())).thenReturn(response);

        // then
        mockMvc.perform(
                        get("/friend")
                                .param("ids", followingIds.get(0).toString())
                                .param("ids", followingIds.get(1).toString()))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.code")
                                .value(FollowSuccessType.CHECK_FOLLOWING_SUCCESS.getCode()))
                .andExpect(
                        jsonPath("$.message")
                                .value(FollowSuccessType.CHECK_FOLLOWING_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data.followingList").isArray());
    }
}
