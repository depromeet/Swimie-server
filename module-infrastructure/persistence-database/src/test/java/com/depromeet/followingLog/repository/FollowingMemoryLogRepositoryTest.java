package com.depromeet.followingLog.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.depromeet.TestQueryDslConfig;
import com.depromeet.fixture.member.MemberFixture;
import com.depromeet.fixture.memory.MemoryDetailFixture;
import com.depromeet.fixture.memory.MemoryFixture;
import com.depromeet.followingLog.domain.FollowingLogSlice;
import com.depromeet.followingLog.domain.FollowingMemoryLog;
import com.depromeet.followingLog.port.out.persistence.FollowingMemoryLogPersistencePort;
import com.depromeet.friend.domain.Friend;
import com.depromeet.friend.port.out.persistence.FriendPersistencePort;
import com.depromeet.friend.repository.FriendJpaRepository;
import com.depromeet.friend.repository.FriendRepository;
import com.depromeet.member.domain.Member;
import com.depromeet.member.entity.MemberEntity;
import com.depromeet.member.port.out.persistence.MemberPersistencePort;
import com.depromeet.member.repository.MemberJpaRepository;
import com.depromeet.member.repository.MemberRepository;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.MemoryDetail;
import com.depromeet.memory.port.out.persistence.MemoryDetailPersistencePort;
import com.depromeet.memory.port.out.persistence.MemoryPersistencePort;
import com.depromeet.memory.repository.MemoryDetailJpaRepository;
import com.depromeet.memory.repository.MemoryDetailRepository;
import com.depromeet.memory.repository.MemoryJpaRepository;
import com.depromeet.memory.repository.MemoryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
public class FollowingMemoryLogRepositoryTest {
    @Autowired private JPAQueryFactory queryFactory;
    @Autowired private FriendJpaRepository friendJpaRepository;
    @Autowired private MemberJpaRepository memberJpaRepository;
    @Autowired private MemoryJpaRepository memoryJpaRepository;
    @Autowired private MemoryDetailJpaRepository memoryDetailJpaRepository;
    @Autowired private FollowingMemoryLogJpaRepository followingMemoryLogJpaRepository;

    private MemberPersistencePort memberRepository;
    private FriendPersistencePort friendRepository;
    private MemoryPersistencePort memoryRepository;
    private MemoryDetailPersistencePort memoryDetailRepository;
    private FollowingMemoryLogPersistencePort followingMemoryLogRepository;

    Member member;
    List<Member> members;

    @BeforeEach
    void setUp() {
        memberRepository = new MemberRepository(queryFactory, memberJpaRepository);
        friendRepository = new FriendRepository(queryFactory, friendJpaRepository);
        memoryRepository = new MemoryRepository(queryFactory, memoryJpaRepository);
        memoryDetailRepository = new MemoryDetailRepository(memoryDetailJpaRepository);
        followingMemoryLogRepository =
                new FollowingMemoryLogRepository(queryFactory, followingMemoryLogJpaRepository);

        member = MemberFixture.make("user", "user@gmail.com", "google 1234");
        member = memberRepository.save(member);
        members = saveMembers();
        saveFollowers(members);
    }

    @AfterEach
    void clear() {
        followingMemoryLogJpaRepository.deleteAll();
        memoryJpaRepository.deleteAll();
        memoryDetailJpaRepository.deleteAll();
        friendJpaRepository.deleteAll();
        memberJpaRepository.deleteAll();
    }

    @Test
    void 팔로잉_소식_저장() {
        // given
        Member member1 = members.getFirst();
        Memory memory = saveMemory();

        FollowingMemoryLog followingMemoryLog =
                FollowingMemoryLog.builder().member(member1).memory(memory).build();

        // when
        followingMemoryLog = followingMemoryLogRepository.save(followingMemoryLog);

        // then
        assertThat(followingMemoryLog.getId()).isNotNull();
    }

    @Test
    void 팔로잉_소식_조회() {
        // given
        List<Memory> memories = saveMemories();
        List<FollowingMemoryLog> followingMemoryLogs = getFollowingMemoryLogs(memories);
        List<Long> expectedMemberIds =
                followingMemoryLogs.stream().map(f -> f.getMember().getId()).toList();
        List<Long> expectedMemoryIds =
                followingMemoryLogs.stream().map(f -> f.getMemory().getId()).toList();

        // when
        FollowingLogSlice followingLogSlice =
                followingMemoryLogRepository.findLogsByMemberIdAndCursorId(member.getId(), null);

        // then
        List<FollowingMemoryLog> resultContents = followingLogSlice.getContents();
        List<Long> resultMemberIds =
                resultContents.stream().map(f -> f.getMember().getId()).toList();
        List<Long> resultMemoryIds =
                resultContents.stream().map(f -> f.getMemory().getId()).toList();

        assertThat(followingLogSlice.getPageSize()).isEqualTo(10);
        assertThat(resultMemberIds).containsExactlyInAnyOrderElementsOf(expectedMemberIds);
        assertThat(resultMemoryIds).containsExactlyInAnyOrderElementsOf(expectedMemoryIds);
    }

    private List<Member> saveMembers() {
        List<MemberEntity> memberEntities = new ArrayList<>();
        for (int i = 2; i <= 11; i++) {
            Member following =
                    MemberFixture.make("user" + i, "user" + i + "@gmail.com", "google 1234" + i);
            memberEntities.add(MemberEntity.from(following));
        }
        return memberJpaRepository.saveAll(memberEntities).stream()
                .map(MemberEntity::toModel)
                .toList();
    }

    private List<Friend> saveFollowers(List<Member> followings) {
        List<Friend> friends = new ArrayList<>();

        for (Member following : followings) {
            Friend friend = Friend.builder().member(member).following(following).build();
            friend = friendRepository.addFollow(friend);
            friends.add(friend);
        }
        return friends;
    }

    private Memory saveMemory() {
        MemoryDetail memoryDetail = MemoryDetailFixture.make();
        memoryDetail = memoryDetailRepository.save(memoryDetail);

        LocalDate recordAt = LocalDate.of(2024, 7, 1);
        Memory memory = MemoryFixture.make(member, memoryDetail, null, recordAt);

        return memoryRepository.save(memory);
    }

    private List<Memory> saveMemories() {
        List<MemoryDetail> memoryDetails = MemoryDetailFixture.makeMemoryDetails(10);
        List<Memory> memories = new ArrayList<>();

        for (int i = 0; i < memoryDetails.size(); i++) {
            MemoryDetail memoryDetail = memoryDetailRepository.save(memoryDetails.get(i));
            Memory memory =
                    MemoryFixture.make(
                            members.get(i), memoryDetail, null, LocalDate.of(2024, 7, 1));
            memory = memoryRepository.save(memory);
            memories.add(memory);
        }

        return memories;
    }

    private List<FollowingMemoryLog> getFollowingMemoryLogs(List<Memory> memories) {
        List<FollowingMemoryLog> followingMemoryLogs = new ArrayList<>();
        for (int i = 0; i < memories.size(); i++) {
            FollowingMemoryLog followingMemoryLog =
                    FollowingMemoryLog.builder()
                            .member(members.get(i))
                            .memory(memories.get(i))
                            .build();
            followingMemoryLog = followingMemoryLogRepository.save(followingMemoryLog);
            followingMemoryLogs.add(followingMemoryLog);
        }
        return followingMemoryLogs;
    }
}
