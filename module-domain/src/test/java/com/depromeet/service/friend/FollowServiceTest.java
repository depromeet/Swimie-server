package com.depromeet.service.friend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.depromeet.exception.BadRequestException;
import com.depromeet.fixture.domain.member.MemberFixture;
import com.depromeet.friend.domain.vo.*;
import com.depromeet.friend.port.in.command.DeleteFollowCommand;
import com.depromeet.friend.service.FollowService;
import com.depromeet.member.domain.Member;
import com.depromeet.mock.friend.FakeFriendRepository;
import com.depromeet.type.friend.FollowErrorType;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FollowServiceTest {
    private FakeFriendRepository friendRepository;
    private FollowService followService;

    @BeforeEach
    void init() {
        friendRepository = new FakeFriendRepository();
        followService = new FollowService(friendRepository);
    }

    @Test
    void 팔로우_추가() {
        // given
        Member member = MemberFixture.make(1L);
        Member following = MemberFixture.make(2L);

        // when
        boolean isFollowed = followService.addOrDeleteFollow(member, following);

        // then
        assertThat(isFollowed).isTrue();
    }

    @Test
    void 팔로우_취소() {
        // given
        Member member = MemberFixture.make(1L);
        Member following = MemberFixture.make(2L);

        // when
        followService.addOrDeleteFollow(member, following);
        boolean isFollowed = followService.addOrDeleteFollow(member, following);

        // then
        assertThat(isFollowed).isFalse();
    }

    @Test
    void 자기_자신을_팔로우시_예외_처리() {
        // given
        Member member = MemberFixture.make(1L);

        // then
        assertThatThrownBy(() -> followService.addOrDeleteFollow(member, member))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(FollowErrorType.SELF_FOLLOWING_NOT_ALLOWED.getMessage());
    }

    @Test
    void 팔로잉_조회() {
        // given
        Long memberId = 1L;
        Member member = MemberFixture.make(memberId);
        List<Long> followingIds = new ArrayList<>();
        for (long i = 2L; i < 20L; i++) {
            Member following = MemberFixture.make(i);
            followService.addOrDeleteFollow(member, following);
            if (i >= 10L) {
                followingIds.add(following.getId());
            }
        }
        followingIds.sort(Comparator.reverseOrder());

        // when
        FollowSlice<Following> followSlice =
                followService.getFollowingByMemberIdAndCursorId(memberId, null);

        // then
        List<Following> followings = followSlice.getFollowContents();
        List<Long> actualFollowingIds =
                followings.stream().mapToLong(Following::getMemberId).boxed().toList();

        assertThat(followingIds).containsExactlyInAnyOrderElementsOf(actualFollowingIds);
        assertThat(followSlice.isHasNext()).isTrue();
        assertThat(followings.size()).isEqualTo(10);
    }

    @Test
    void 팔로워_조회() {
        // given
        Long memberId = 1L;
        Member member = MemberFixture.make(memberId);
        List<Long> followersId = new ArrayList<>();
        for (long i = 2L; i < 20L; i++) {
            Member followers = MemberFixture.make(i);
            followService.addOrDeleteFollow(followers, member);
            if (i >= 10L) {
                followersId.add(followers.getId());
            }
        }
        followersId.sort(Comparator.reverseOrder());

        // when
        FollowSlice<Follower> followSlice =
                followService.getFollowerByMemberIdAndCursorId(memberId, null);

        // then
        List<Follower> followers = followSlice.getFollowContents();
        List<Long> actualFollowingIds =
                followers.stream().mapToLong(Follower::getMemberId).boxed().toList();

        assertThat(followersId).containsExactlyInAnyOrderElementsOf(actualFollowingIds);
        assertThat(followSlice.isHasNext()).isTrue();
        assertThat(followers.size()).isEqualTo(10);
    }

    @Test
    void 팔로잉_수_세기() {
        // given
        Long memberId = 1L;
        Member member = MemberFixture.make(memberId);
        int expectedFollowingCount = 0;
        for (long i = 2L; i < 20L; i++) {
            Member following = MemberFixture.make(i);
            followService.addOrDeleteFollow(member, following);
            expectedFollowingCount++;
        }

        // when
        int actualFollowingCount = followService.countFollowingByMemberId(memberId);

        // then
        assertThat(actualFollowingCount).isEqualTo(expectedFollowingCount);
    }

    @Test
    void 팔로워_수_세기() {
        // given
        Long memberId = 1L;
        Member member = MemberFixture.make(memberId);
        int expectedFollowerCount = 0;
        for (long i = 2L; i < 20L; i++) {
            Member follower = MemberFixture.make(i);
            followService.addOrDeleteFollow(follower, member);
            expectedFollowerCount++;
        }

        // when
        int actualFollowerCount = followService.countFollowerByMemberId(memberId);

        // then
        assertThat(actualFollowerCount).isEqualTo(expectedFollowerCount);
    }

    @Test
    void 팔로잉_3명_조회() {
        // given
        Long memberId = 1L;
        Member member = MemberFixture.make(memberId);
        for (long i = 2L; i < 20L; i++) {
            Member following = MemberFixture.make(i);
            followService.addOrDeleteFollow(member, following);
        }

        // when
        List<Following> followings = followService.getFollowingByMemberIdLimitThree(memberId);

        // then
        List<Long> actualFollowingIds =
                followings.stream().mapToLong(Following::getMemberId).boxed().toList();
        assertThat(followings.size()).isEqualTo(3);
        assertThat(actualFollowingIds).containsExactly(19L, 18L, 17L);
    }

    @Test
    void 팔로워_팔로우_개수_세기() {
        // given
        Long memberId = 1L;
        Member member = MemberFixture.make(memberId);
        int followingCnt = 0;
        int followerCnt = 0;
        for (long i = 2L; i < 20L; i++) {
            Member following = MemberFixture.make(i);
            followService.addOrDeleteFollow(following, member);
            followService.addOrDeleteFollow(member, following);

            followingCnt++;
            followerCnt++;
        }

        // when
        FriendCount friendCount = followService.countFriendByMemberId(memberId);

        // then
        assertThat(friendCount.followingCount()).isEqualTo(followingCnt);
        assertThat(friendCount.followerCount()).isEqualTo(followerCnt);
    }

    @Test
    void Friend_삭제() {
        // given
        Long memberId = 1L;
        Member member = MemberFixture.make(memberId);

        for (long i = 2L; i < 10L; i++) {
            Member following = MemberFixture.make(i);
            followService.addOrDeleteFollow(following, member);
            followService.addOrDeleteFollow(member, following);
        }

        int beforeDeleteFollowingCnt = followService.countFollowingByMemberId(memberId);
        int beforeDeleteFollowerCnt = followService.countFollowerByMemberId(memberId);

        assertThat(beforeDeleteFollowingCnt).isGreaterThan(0);
        assertThat(beforeDeleteFollowerCnt).isGreaterThan(0);

        // when
        followService.deleteByMemberId(memberId);

        // then
        int afterDeleteFollowingCnt = followService.countFollowingByMemberId(memberId);
        int afterDeleteFollowerCnt = followService.countFollowerByMemberId(memberId);

        assertThat(afterDeleteFollowingCnt).isEqualTo(0);
        assertThat(afterDeleteFollowerCnt).isEqualTo(0);
    }

    @Test
    void 팔로우_유무_확인() {
        // given
        Long memberId = 1L;
        Member member = MemberFixture.make(memberId);

        List<Long> memberIds = new ArrayList<>();
        List<Long> followingIds = new ArrayList<>();
        List<Long> notFollowingIds = new ArrayList<>();

        for (long i = 2L; i < 10L; i++) {
            memberIds.add(i);
            if (i % 2 == 0) {
                Member following = MemberFixture.make(i);
                followService.addOrDeleteFollow(member, following);
                followingIds.add(following.getId());
            } else {
                notFollowingIds.add(i);
            }
        }

        List<FollowCheck> followChecks = followService.checkFollowingState(memberId, memberIds);

        List<Long> actualFollowingIds =
                followChecks.stream()
                        .filter(FollowCheck::isFollowing)
                        .mapToLong(FollowCheck::targetId)
                        .boxed()
                        .toList();
        List<Long> actualNotFollowingIds =
                followChecks.stream()
                        .filter(item -> !item.isFollowing())
                        .mapToLong(FollowCheck::targetId)
                        .boxed()
                        .toList();

        assertThat(actualFollowingIds).containsExactlyElementsOf(followingIds);
        assertThat(actualNotFollowingIds).containsExactlyElementsOf(notFollowingIds);
    }

    @Test
    void 블랙리스트_등록() {
        // given
        Long memberId = 1L;
        Long followingId = 2L;

        Member member = MemberFixture.make(memberId);
        Member following = MemberFixture.make(followingId);
        followService.addOrDeleteFollow(following, member);
        followService.addOrDeleteFollow(member, following);

        int beforeDeleteFollowingCnt = followService.countFollowingByMemberId(memberId);
        int beforeDeleteFollowerCnt = followService.countFollowerByMemberId(memberId);

        assertThat(beforeDeleteFollowingCnt).isEqualTo(1);
        assertThat(beforeDeleteFollowerCnt).isEqualTo(1);

        // when
        followService.deleteBlackMemberInFollowList(new DeleteFollowCommand(memberId, followingId));

        // then
        int afterDeleteFollowingCnt = followService.countFollowingByMemberId(memberId);
        int afterDeleteFollowerCnt = followService.countFollowerByMemberId(memberId);

        assertThat(afterDeleteFollowingCnt).isEqualTo(0);
        assertThat(afterDeleteFollowerCnt).isEqualTo(0);
    }
}
