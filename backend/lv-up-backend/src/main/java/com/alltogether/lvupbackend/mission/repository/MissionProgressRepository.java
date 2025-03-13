package com.alltogether.lvupbackend.mission.repository;

import com.alltogether.lvupbackend.user.domain.Mission;
import com.alltogether.lvupbackend.user.domain.MissionProgress;
import com.alltogether.lvupbackend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MissionProgressRepository extends JpaRepository<MissionProgress, Integer> {
    Optional<MissionProgress> findByUserAndMission(User user, Mission mission);

    List<MissionProgress> findByUser_UserIdAndMission_CategoryAndCreatedAtBetween(
            Integer userId,
            char category,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    Optional<MissionProgress> findByUserAndMissionAndCreatedAtBetween(
            User user,
            Mission mission,
            LocalDateTime startDate,
            LocalDateTime endOfDay
    );

    // 특정 카테고리의 미션 진행 상황 삭제를 위한 메서드 추가
    @Modifying
    @Query("DELETE FROM MissionProgress mp WHERE mp.mission.category = :category")
    long deleteByMissionCategory(@Param("category") char category);

    // 사용자 ID와 카테고리로 미션 진행 상황 삭제
    @Modifying
    @Query("DELETE FROM MissionProgress mp WHERE mp.user.userId = :userId AND mp.mission.category = :category")
    void deleteByUser_UserIdAndMission_Category(
            @Param("userId") Integer userId,
            @Param("category") char category
    );
}