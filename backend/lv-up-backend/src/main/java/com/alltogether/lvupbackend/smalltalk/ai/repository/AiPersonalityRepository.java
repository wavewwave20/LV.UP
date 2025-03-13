package com.alltogether.lvupbackend.smalltalk.ai.repository;

import com.alltogether.lvupbackend.smalltalk.ai.entity.AiPersonality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiPersonalityRepository extends JpaRepository<AiPersonality, Integer> {
}
