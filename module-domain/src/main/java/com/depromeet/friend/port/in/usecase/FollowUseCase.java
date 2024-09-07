package com.depromeet.friend.port.in.usecase;

import com.depromeet.friend.domain.vo.*;
import com.depromeet.friend.port.in.command.DeleteFollowCommand;
import com.depromeet.member.domain.Member;
import java.util.List;

public interface FollowUseCase {
    boolean addOrDeleteFollow(Member member, Member following);

    FollowSlice<Following> getFollowingByMemberIdAndCursorId(Long memberId, Long cursorId);

    FollowSlice<Follower> getFollowerByMemberIdAndCursorId(Long memberId, Long cursorId);

    int countFollowingByMemberId(Long memberId);

    int countFollowerByMemberId(Long memberId);

    List<Following> getFollowingByMemberIdLimitThree(Long memberId);

    FriendCount countFriendByMemberId(Long memberId);

    void deleteByMemberId(Long memberId);

    List<FollowCheck> checkFollowingState(Long memberId, List<Long> targetIds);

    void deleteBlackMemberInFollowList(DeleteFollowCommand deleteFollowCommand);
}
