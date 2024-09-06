package com.depromeet.blacklist.repository;

import com.depromeet.blacklist.domain.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistJpaRepository extends JpaRepository<Blacklist, Long> {}
