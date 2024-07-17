package com.depromeet.member.service;

import com.depromeet.auth.dto.response.AccountProfileResponse;
import com.depromeet.exception.NotFoundException;
import com.depromeet.member.Member;
import com.depromeet.member.MemberRole;
import com.depromeet.member.dto.request.MemberCreateDto;
import com.depromeet.member.dto.response.MemberFindOneResponseDto;
import com.depromeet.member.mapper.MemberMapper;
import com.depromeet.member.repository.MemberRepository;
import com.depromeet.type.member.MemberErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Deprecated
    @Override
    public Member save(MemberCreateDto memberCreate) {
        Member member = MemberMapper.from(memberCreate);
        return memberRepository.save(member);
    }

    @Override
    public MemberFindOneResponseDto findOneMemberResponseById(Long id) {
        Member member =
                memberRepository
                        .findById(id)
                        .orElseThrow(() -> new NotFoundException(MemberErrorType.NOT_FOUND));
        return MemberFindOneResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }

    @Override
    public Member findById(Long id) {
        return memberRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(MemberErrorType.NOT_FOUND));
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository
                .findByEmail(email)
                .orElseThrow(() -> new NotFoundException(MemberErrorType.NOT_FOUND));
    }

    @Deprecated
    @Override
    public boolean matchPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public Member findOrCreateMemberBy(AccountProfileResponse profile) {
        return memberRepository
                .findByEmail(profile.email())
                .orElseGet(
                        () -> {
                            Member member =
                                    Member.builder()
                                            .name(profile.name())
                                            .email(profile.email())
                                            .role(MemberRole.USER)
                                            .build();
                            return memberRepository.save(member);
                        });
    }
}
