package com.alltogether.lvupbackend.mission.repository;

import com.alltogether.lvupbackend.user.domain.Mission;
import com.alltogether.lvupbackend.user.domain.MissionProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Integer> {
    Optional<Mission> findByName(String name);
    List<Mission> findByCategory(char category);
    List<Mission> findByEnabledTrueAndCategory(char category);

    List<Mission> findByEnabledTrueAndCategoryAndMissionIdNot(Character category, Integer excludeMissionId);
}
