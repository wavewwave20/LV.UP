package com.alltogether.lvupbackend.mission.controller;

import com.alltogether.lvupbackend.mission.dto.MissionDto;
import com.alltogether.lvupbackend.mission.service.MissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mission")
@Slf4j
public class MissionController {
    private final MissionService missionService;

    // 현재 로그인한 사용자 ID 추출
    private Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Integer.parseInt(authentication.getName());
    }

    @GetMapping("/list")
    public ResponseEntity<List<MissionDto>> getAllMissions() {
        List<MissionDto> missions = missionService.getAllMissions();
        return ResponseEntity.ok(missions);
    }


    // 보상 수령
    @PostMapping("/reward/{id}")
    public ResponseEntity<?> claimMissionReward(@PathVariable("id") Integer missionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = Integer.parseInt(authentication.getName());

        try {
            missionService.claimMissionReward(userId, missionId);
            return ResponseEntity.ok("미션 보상이 성공적으로 지급되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 오늘의 미션
    @GetMapping("/today")
    public ResponseEntity<List<MissionDto>> getTodayMissions() {
        Integer userId = getCurrentUserId();
        List<MissionDto> missions = missionService.getTodaysDailyMissions(userId);
        return ResponseEntity.ok(missions);
    }

}
