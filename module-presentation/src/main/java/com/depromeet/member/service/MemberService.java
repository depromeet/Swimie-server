package com.depromeet.member.service;

import com.depromeet.member.Member;
import com.depromeet.member.dto.request.MemberCreateDto;
import com.depromeet.member.dto.response.MemberFindOneResponseDto;

public interface MemberService {
    Member save(MemberCreateDto memberCreate);

    MemberFindOneResponseDto findOneMemberResponseById(Long id);

    Member findById(Long id);

    Member findByEmail(String email);

    boolean matchPassword(String rawPassword, String encodedPassword);
}
