package com.depromeet.friend.service;

import com.depromeet.exception.BadRequestException;
import com.depromeet.friend.domain.Friend;
import com.depromeet.friend.domain.vo.*;
import com.depromeet.friend.port.in.command.DeleteFollowCommand;
import com.depromeet.friend.port.in.usecase.FollowUseCase;
import com.depromeet.friend.port.out.persistence.FriendPersistencePort;
import com.depromeet.member.domain.Member;
import com.depromeet.type.friend.FollowErrorType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService implements FollowUseCase {
    private final FriendPersistencePort friendPersistencePort;

    @Transactional
    public boolean addOrDeleteFollow(Member member, Member following) {
        validatefollowingSelf(member.getId(), following.getId());

        boolean followingIsExists = checkFollowingExist(member.getId(), following.getId());
        if (followingIsExists) {
            friendPersistencePort.deleteByMemberIdAndFollowingId(member.getId(), following.getId());
            return false;
        }
        Friend friend = Friend.builder().member(member).following(following).build();
        friendPersistencePort.addFollow(friend);
        return true;
    }

    private void validatefollowingSelf(Long memberId, Long followingId) {
        if (memberId.equals(followingId)) {
            throw new BadRequestException(FollowErrorType.SELF_FOLLOWING_NOT_ALLOWED);
        }
    }

    private boolean checkFollowingExist(Long memberId, Long followingId) {
        Optional<Friend> existedFollowing =
                friendPersistencePort.findByMemberIdAndFollowingId(memberId, followingId);
        return existedFollowing.isPresent();
    }

    @Override
    public FollowSlice<Following> getFollowingByMemberIdAndCursorId(Long memberId, Long cursorId) {
        List<Following> followings =
                friendPersistencePort.findFollowingsByMemberIdAndCursorId(memberId, cursorId);

        boolean hasNext = false;
        Long nextCursorId = null;
        if (followings.size() > 10) {
            followings = new ArrayList<>(followings);
            followings.removeLast();
            hasNext = true;
            nextCursorId = followings.getLast().getFriendId();
        }
        return FollowSlice.<Following>builder()
                .followContents(followings)
                .cursorId(nextCursorId)
                .hasNext(hasNext)
                .build();
    }

    @Override
    public FollowSlice<Follower> getFollowerByMemberIdAndCursorId(Long memberId, Long cursorId) {
        List<Follower> followers =
                friendPersistencePort.findFollowersByMemberIdAndCursorId(memberId, cursorId);

        boolean hasNext = false;
        Long nextCursorId = null;
        if (followers.size() > 10) {
            followers = new ArrayList<>(followers);
            followers.removeLast();
            hasNext = true;
            nextCursorId = followers.getLast().getFriendId();
        }
        return FollowSlice.<Follower>builder()
                .followContents(followers)
                .cursorId(nextCursorId)
                .hasNext(hasNext)
                .build();
    }

    @Override
    public int countFollowingByMemberId(Long memberId) {
        return friendPersistencePort.countFollowingByMemberId(memberId);
    }

    @Override
    public int countFollowerByMemberId(Long memberId) {
        return friendPersistencePort.countFollowerByMemberId(memberId);
    }

    @Override
    public List<Following> getFollowingByMemberIdLimitThree(Long memberId) {
        return friendPersistencePort.findFollowingByMemberIdLimitThree(memberId);
    }

    @Override
    public FriendCount countFriendByMemberId(Long memberId) {
        return friendPersistencePort.countFriendByMemberId(memberId);
    }

    @Override
    public void deleteByMemberId(Long memberId) {
        friendPersistencePort.deleteByMemberId(memberId);
    }

    @Override
    public List<FollowCheck> checkFollowingState(Long memberId, List<Long> targetIds) {
        return friendPersistencePort.findByMemberIdAndFollowingIds(memberId, targetIds);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void deleteBlackMemberInFollowList(DeleteFollowCommand deleteFollowCommand) {
        Long requesterId = deleteFollowCommand.requesterId();
        Long blackMemberId = deleteFollowCommand.blackMemberId();

        friendPersistencePort.deleteFollowerFollowingByMemberIdAndFollowingId(
                requesterId, blackMemberId);
    }
}
