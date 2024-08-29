package com.depromeet.following.dto.response;

import com.depromeet.friend.dto.response.FollowerResponse;
import java.util.ArrayList;
import java.util.List;

public class FollowerResponseFixture {
    public static List<FollowerResponse> make() {
        List<FollowerResponse> followerResponses = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            followerResponses.add(FollowerResponse.builder().build());
        }
        return followerResponses;
    }
}
