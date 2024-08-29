package com.depromeet.friend.fixture.response;

import com.depromeet.friend.dto.response.FollowingResponse;
import java.util.ArrayList;
import java.util.List;

public class FollowingResponseFixture {
    public static List<FollowingResponse> make() {
        List<FollowingResponse> followingResponses = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            followingResponses.add(FollowingResponse.builder().build());
        }
        return followingResponses;
    }
}
