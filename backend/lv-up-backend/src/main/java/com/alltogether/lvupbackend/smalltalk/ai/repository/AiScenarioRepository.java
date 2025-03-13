package com.alltogether.lvupbackend.smalltalk.ai.repository;

import com.alltogether.lvupbackend.smalltalk.ai.entity.AiScenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiScenarioRepository extends JpaRepository<AiScenario, Integer> {
}
