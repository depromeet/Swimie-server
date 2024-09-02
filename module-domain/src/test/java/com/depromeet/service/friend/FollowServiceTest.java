package com.depromeet.service.friend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.depromeet.exception.BadRequestException;
import com.depromeet.fixture.domain.member.MemberFixture;
import com.depromeet.friend.service.FollowService;
import com.depromeet.member.domain.Member;
import com.depromeet.mock.friend.FakeFriendRepository;
import com.depromeet.type.friend.FollowErrorType;
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
        Member member = MemberFixture.make(1L, "USER");
        Member following = MemberFixture.make(2L, "USER");

        // when
        boolean isFollowed = followService.addOrDeleteFollow(member, following);

        // then
        assertThat(isFollowed).isTrue();
    }

    @Test
    void 팔로우_취소() {
        // given
        Member member = MemberFixture.make(1L, "USER");
        Member following = MemberFixture.make(2L, "USER");

        // when
        followService.addOrDeleteFollow(member, following);
        boolean isFollowed = followService.addOrDeleteFollow(member, following);

        // then
        assertThat(isFollowed).isFalse();
    }

    @Test
    void 자기_자신을_팔로우시_예외_처리() {
        // given
        Member member = MemberFixture.make(1L, "USER");

        // then
        assertThatThrownBy(() -> followService.addOrDeleteFollow(member, member))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(FollowErrorType.SELF_FOLLOWING_NOT_ALLOWED.getMessage());
    }
}
