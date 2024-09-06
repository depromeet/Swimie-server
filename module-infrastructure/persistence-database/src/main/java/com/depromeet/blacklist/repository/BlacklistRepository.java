package com.depromeet.blacklist.repository;

import com.depromeet.blacklist.port.out.BlacklistPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BlacklistRepository implements BlacklistPersistencePort {
    private final BlacklistJpaRepository blacklistJpaRepository;
}
