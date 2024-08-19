package com.depromeet.member.port.out.persistence;

import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberGender;
import com.depromeet.member.domain.vo.MemberSearchPage;
import com.depromeet.member.port.in.command.UpdateMemberCommand;
import java.util.Optional;

public interface MemberPersistencePort {
    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);

    Member save(Member member);

    Optional<Member> updateGoal(Long memberId, Integer goal);

    Optional<Member> updateNickname(Long memberId, String nickname);

    Optional<Member> findByProviderId(String providerId);

    Optional<Member> updateGender(Long memberId, MemberGender gender);

    void deleteById(Long id);

    MemberSearchPage searchByNameQuery(Long memberId, String nameQuery, Long cursorId);

    Optional<Member> updateLatestViewedFollowingLogAt(Long memberId);

    Optional<Member> update(UpdateMemberCommand command);

    Optional<Member> updateProfileImageUrl(Long memberId, String profileImageUrl);
}
