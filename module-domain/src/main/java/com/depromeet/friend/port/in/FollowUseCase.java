package com.depromeet.friend.port.in;

import com.depromeet.friend.domain.vo.FollowSlice;
import com.depromeet.friend.domain.vo.Follower;
import com.depromeet.friend.domain.vo.Following;
import com.depromeet.member.domain.Member;

public interface FollowUseCase {
    boolean addOrDeleteFollowing(Member member, Member following);

    FollowSlice<Following> getFollowingByMemberIdAndCursorId(Long memberId, Long cursorId);

    FollowSlice<Follower> getFollowerByMemberIdAndCursorId(Long memberId, Long cursorId);

    int countFollowingByMemberId(Long memberId);

    int countFollowerByMemberId(Long memberId);
}
