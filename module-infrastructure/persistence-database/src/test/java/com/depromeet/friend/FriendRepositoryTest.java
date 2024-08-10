package com.depromeet.friend;

import static org.assertj.core.api.Assertions.assertThat;

import com.depromeet.TestQueryDslConfig;
import com.depromeet.fixture.member.MemberFixture;
import com.depromeet.friend.domain.Friend;
import com.depromeet.friend.repository.FriendJpaRepository;
import com.depromeet.friend.repository.FriendRepository;
import com.depromeet.member.domain.Member;
import com.depromeet.member.repository.MemberJpaRepository;
import com.depromeet.member.repository.MemberRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@Import(TestQueryDslConfig.class)
@ExtendWith(SpringExtension.class)
public class FriendRepositoryTest {
    @Autowired private JPAQueryFactory queryFactory;
    @Autowired private FriendJpaRepository friendJpaRepository;
    @Autowired private MemberJpaRepository memberJpaRepository;

    private MemberRepository memberRepository;
    private FriendRepository friendRepository;

    Member member;
    Member following;

    @BeforeEach
    void setUp() {
        memberRepository = new MemberRepository(memberJpaRepository);
        friendRepository = new FriendRepository(queryFactory, friendJpaRepository);

        member = MemberFixture.make("user", "user@gmail.com", "google 1234");
        following = MemberFixture.make("following", "following@gmail.com", "google 5678");

        member = memberRepository.save(member);
        following = memberRepository.save(following);
    }

    @AfterEach
    void clear() {
        friendJpaRepository.deleteAll();
    }

    @Test
    void 팔로잉을_추가한다() {
        // given, when
        Long friendId = saveFriend();

        // then
        Optional<Friend> resultFriend = friendRepository.findById(friendId);
        assertThat(resultFriend).isPresent();

        assertThat(resultFriend.get().getId()).isNotNull();
        assertThat(resultFriend.get().getMember().getId()).isEqualTo(member.getId());
        assertThat(resultFriend.get().getFollowing().getId()).isEqualTo(following.getId());
    }

    private Long saveFriend() {
        Friend friend = Friend.builder().member(member).following(following).build();

        return friendRepository.addFollowing(friend);
    }

    @Test
    void 사용자_ID와_팔로잉_ID로_Friend를_조회한다() {
        // given
        saveFriend();

        Long memberId = member.getId();
        Long followingId = following.getId();

        // when
        Optional<Friend> resultFriend =
                friendRepository.findByMemberIdAndFollowingId(memberId, followingId);

        // then
        assertThat(resultFriend).isPresent();
        assertThat(resultFriend.get().getMember().getId()).isEqualTo(memberId);
        assertThat(resultFriend.get().getFollowing().getId()).isEqualTo(followingId);
    }

    @Test
    void Friend객체를_memberId와_following_id로_삭제한다() {
        // given
        saveFriend();
        assertThat(friendJpaRepository.findAll()).isNotEmpty();

        // when
        Long memberId = member.getId();
        Long followingId = following.getId();
        friendRepository.deleteByMemberIdAndFollowingId(memberId, followingId);

        // then
        assertThat(friendJpaRepository.findAll()).isEmpty();
    }
}
