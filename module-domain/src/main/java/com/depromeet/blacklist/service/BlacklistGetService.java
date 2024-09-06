package com.depromeet.blacklist.service;

import com.depromeet.blacklist.port.in.BlacklistGetUseCase;
import com.depromeet.blacklist.port.out.BlacklistPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlacklistGetService implements BlacklistGetUseCase {
    private final BlacklistPersistencePort blacklistPersistencePort;
}
