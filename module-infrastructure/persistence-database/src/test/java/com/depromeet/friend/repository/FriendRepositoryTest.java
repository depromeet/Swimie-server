package com.depromeet.friend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.depromeet.TestQueryDslConfig;
import com.depromeet.fixture.domain.member.MemberFixture;
import com.depromeet.friend.domain.Friend;
import com.depromeet.friend.domain.vo.Follower;
import com.depromeet.friend.domain.vo.Following;
import com.depromeet.member.domain.Member;
import com.depromeet.member.entity.MemberEntity;
import com.depromeet.member.repository.MemberJpaRepository;
import com.depromeet.member.repository.MemberRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
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
        memberRepository = new MemberRepository(queryFactory, memberJpaRepository);
        friendRepository = new FriendRepository(queryFactory, friendJpaRepository);

        member = MemberFixture.make("user", "user@gmail.com", "google 1234");
        member = memberRepository.save(member);

        following = MemberFixture.make("following", "following@gmail.com", "google 5678");
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
        return friendRepository.addFollow(friend).getId();
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

    @Test
    void 팔로잉_리스트를_조회한다() {
        // given
        List<Member> followings = saveMembers();
        List<Friend> friends = saveFollowings(followings);
        friends = friends.reversed();
        friends = friends.subList(0, 10);

        List<Following> expectedFollowing = getExpectedFollowing(friends);
        List<String> expectedFollowingNames =
                expectedFollowing.stream().map(Following::getName).toList();

        // when
        List<Following> result =
                friendRepository.findFollowingsByMemberIdAndCursorId(member.getId(), null);
        List<String> resultFollowingNames = result.stream().map(Following::getName).toList();

        if (resultFollowingNames.size() > 10) {
            resultFollowingNames.removeLast();
        }
        // then
        assertThat(resultFollowingNames)
                .containsExactlyInAnyOrderElementsOf(expectedFollowingNames);
    }

    @Test
    void 팔로워_리스트를_조회한다() {
        // given
        List<Member> followers = saveMembers();
        List<Friend> friends = saveFollowers(followers);
        friends = friends.reversed();
        friends = friends.subList(0, 10);

        List<Follower> expectedFollowers =
                friends.stream()
                        .map(
                                friend ->
                                        Follower.builder()
                                                .friendId(friend.getId())
                                                .memberId(friend.getMember().getId())
                                                .name(friend.getMember().getNickname())
                                                .hasFollowedBack(
                                                        friend.getMember().getId() % 2 == 0)
                                                .build())
                        .toList();
        List<String> expectedFollowerNames =
                expectedFollowers.stream().map(Follower::getName).toList();

        // when
        List<Follower> result =
                friendRepository.findFollowersByMemberIdAndCursorId(member.getId(), null);
        List<String> resultFollowerNames = result.stream().map(Follower::getName).toList();
        if (resultFollowerNames.size() > 10) {
            resultFollowerNames.removeLast();
        }

        // then
        assertThat(resultFollowerNames).containsExactlyInAnyOrderElementsOf(expectedFollowerNames);
    }

    @Test
    void 팔로잉_수를_센다() {
        // given
        List<Member> followings = saveMembers();
        List<Friend> friends = saveFollowings(followings);

        // when
        int count = friendRepository.countFollowingByMemberId(member.getId());

        // then
        assertThat(count).isEqualTo(friends.size());
    }

    @Test
    void 팔로워_수를_센다() {
        // given
        List<Member> followers = saveMembers();
        List<Friend> friends = saveFollowers(followers);

        // when
        int count = friendRepository.countFollowerByMemberId(member.getId());

        // then
        assertThat(count).isEqualTo(friends.size());
    }

    private List<Member> saveMembers() {
        List<Member> members = MemberFixture.makeMembers(15);
        List<MemberEntity> memberEntities = members.stream().map(MemberEntity::from).toList();

        return memberJpaRepository.saveAll(memberEntities).stream()
                .map(MemberEntity::toModel)
                .toList();
    }

    private List<Friend> saveFollowings(List<Member> followings) {
        List<Friend> friends = new ArrayList<>();

        for (Member following : followings) {
            Friend friend = Friend.builder().member(member).following(following).build();
            friend = friendRepository.addFollow(friend);
            friends.add(friend);
        }
        return friends;
    }

    private List<Friend> saveFollowers(List<Member> followers) {
        List<Friend> friends = new ArrayList<>();

        for (Member follower : followers) {
            Friend friend = Friend.builder().member(follower).following(member).build();
            friend = friendRepository.addFollow(friend);
            friends.add(friend);

            // member PK가 짝수인 경우 맞팔로우
            if (follower.getId() % 2 == 0) {
                Friend followedBack = Friend.builder().member(member).following(follower).build();
                friendRepository.addFollow(followedBack);
            }
        }
        return friends;
    }

    private List<Following> getExpectedFollowing(List<Friend> friends) {
        return friends.stream()
                .map(
                        friend ->
                                Following.builder()
                                        .friendId(friend.getId())
                                        .memberId(friend.getFollowing().getId())
                                        .name(friend.getFollowing().getNickname())
                                        .build())
                .toList();
    }
}
