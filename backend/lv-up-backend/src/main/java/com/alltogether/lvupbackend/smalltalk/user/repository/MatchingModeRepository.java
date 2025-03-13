package com.alltogether.lvupbackend.smalltalk.user.repository;

import com.alltogether.lvupbackend.smalltalk.user.entity.MatchingMode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingModeRepository extends JpaRepository<MatchingMode, Long> {
}
