package com.alltogether.lvupbackend.smalltalk.ai.repository;

import com.alltogether.lvupbackend.smalltalk.ai.entity.AiHint;
import com.alltogether.lvupbackend.smalltalk.ai.entity.AiSmalltalk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AiHintRepository extends JpaRepository<AiHint, Integer> {
    Optional<AiHint> findByAiSmalltalkAndTurn(AiSmalltalk aiSmalltalk, Integer turn);
}
