package com.depromeet.blacklist.port.in.usecase;

public interface BlacklistQueryUseCase {
    boolean checkBlackMember(Long memberId, Long blackMemberId);
}
