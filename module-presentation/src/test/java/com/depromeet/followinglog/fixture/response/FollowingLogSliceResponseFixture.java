package com.depromeet.followinglog.fixture.response;

import com.depromeet.followinglog.dto.response.FollowingLogSliceResponse;
import java.util.ArrayList;

public class FollowingLogSliceResponseFixture {
    public static FollowingLogSliceResponse make() {
        return FollowingLogSliceResponse.builder()
                .content(new ArrayList<>())
                .pageSize(10)
                .cursorId(11L)
                .hasNext(true)
                .build();
    }
}
