package com.alltogether.lvupbackend.user.repository;

import com.alltogether.lvupbackend.user.domain.Blacklist;
import com.alltogether.lvupbackend.user.domain.BlacklistId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistRepository extends JpaRepository<Blacklist, BlacklistId> {
}
