package com.alltogether.lvupbackend.user.domain;

import com.alltogether.lvupbackend.mission.repository.MissionProgressRepository;
import com.alltogether.lvupbackend.mission.repository.MissionRepository;
import com.alltogether.lvupbackend.user.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class MissionScheduler {
    private final MissionRepository missionRepository;
    private final MissionProgressRepository missionProgressRepository;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정
    @Transactional
    public void resetDailyMissions() {
        LocalDate today = LocalDate.now();

        try {
            // 일일 미션(D) 진행 상황 삭제
            long deletedProgressCount = missionProgressRepository.deleteByMissionCategory('D');

            // 새로운 일일 미션 선택
            List<Mission> dailyMissions = missionRepository.findByEnabledTrueAndCategory('D');

            if (dailyMissions.isEmpty()) {
                log.warn("No daily missions found to reset");
                return;
            }

            Collections.shuffle(dailyMissions);
            List<Mission> selectedMissions = dailyMissions.stream()
                    .limit(3)
                    .collect(Collectors.toList());

            log.info("Daily missions reset. Deleted {} mission progresses. Selected {} new missions",
                    deletedProgressCount, selectedMissions.size());

        } catch (Exception e) {
            log.error("Error resetting daily missions", e);
        }
    }
}