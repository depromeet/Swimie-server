package com.depromeet.service;

import com.depromeet.auth.port.out.persistence.RefreshRedisPersistencePort;
import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberRole;
import com.depromeet.member.port.out.persistence.MemberPersistencePort;
import com.depromeet.member.service.MemberService;
import com.depromeet.mock.FakeMemberRepository;
import com.depromeet.mock.FakeRefreshRedisRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemberServiceTest {
    private MemberPersistencePort fakeMemberRepository;
    private RefreshRedisPersistencePort fakeRefreshRedisPersistencePort;

    private MemberService memberService;

    private Long userId = 1L;
    private Member member;

    @BeforeEach
    void init() {
        fakeMemberRepository = new FakeMemberRepository();
        fakeRefreshRedisPersistencePort = new FakeRefreshRedisRepository();

        // Member create
        member =
                Member.builder()
                        .id(userId)
                        .nickname("member1")
                        .email("member1@gmail.com")
                        .role(MemberRole.USER)
                        .build();
        fakeMemberRepository.save(member);

        memberService = new MemberService(fakeMemberRepository, fakeRefreshRedisPersistencePort);
    }

    @Test
    void 회원의_이름을_수정할_수_있다() {
        // given
        Long memberId = 1L;
        String newName = "테스트";

        // when
        Member member1 = memberService.updateName(memberId, newName);

        // then
        Assertions.assertThat(member1.getNickname()).isEqualTo(newName);
    }

    @Test
    void 회원의_이름은_공백을_허용하지_않는다() {
        // given
        Long memberId = 1L;
        String newName = "";

        // when

        // then
        Assertions.assertThatThrownBy(() -> memberService.updateName(memberId, newName))
                .hasMessage("멤버의 이름은 공백이 허용되지 않습니다");
    }
}
