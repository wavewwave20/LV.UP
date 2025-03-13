package com.alltogether.lvupbackend.mission.service;


import com.alltogether.lvupbackend.user.domain.Attendance;
import com.alltogether.lvupbackend.user.domain.Mission;
import com.alltogether.lvupbackend.user.domain.MissionProgress;
import com.alltogether.lvupbackend.mission.repository.MissionProgressRepository;
import com.alltogether.lvupbackend.mission.repository.MissionRepository;
import com.alltogether.lvupbackend.user.domain.User;
import com.alltogether.lvupbackend.user.repository.AttendanceRepository;
import com.alltogether.lvupbackend.user.repository.UserRepository;
import com.alltogether.lvupbackend.user.service.UserLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import com.alltogether.lvupbackend.mission.dto.MissionDto;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MissionService {
    private final MissionRepository missionRepository;
    private final MissionProgressRepository missionProgressRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final UserLevelService userLevelService;

    @Transactional(readOnly = true)
    public List<MissionDto> getAllMissions() {
        return missionRepository.findAll().stream()
                .map(mission -> MissionDto.builder()
                        .missionId(mission.getMissionId())
                        .name(mission.getName())
                        .description(mission.getDescription())
                        .goal(mission.getGoal())
                        .category(mission.getCategory())
                        .coin(mission.getCoin())
                        .ticket(mission.getTicket())
                        .exp(mission.getExp())
                        .build())
                .collect(Collectors.toList());
    }


    // 미션 보상
    public void claimMissionReward(Integer userId, Integer missionId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("미션을 찾을 수 없습니다."));

        MissionProgress missionProgress = missionProgressRepository
                .findByUserAndMission(user, mission)
                .orElseThrow(() -> new RuntimeException("미션 진행 상황을 찾을 수 없습니다."));

        if (!missionProgress.getIsCompleted()) {
            throw new RuntimeException("미션이 완료되지 않았습니다.");
        }

        if (missionProgress.getRewardClaimed()) {
            throw new RuntimeException("이미 보상을 받았습니다.");
        }

        // 미션별 고유 보상 지급
        user.setCoinQuantity(user.getCoinQuantity() + mission.getCoin());
        user.setTicketQuantity(user.getTicketQuantity() + mission.getTicket());
        user.setTotalExp(user.getTotalExp() + mission.getExp());

        userLevelService.updateUserExperience(userId, mission.getExp());

        missionProgress.setRewardClaimed(true);

        userRepository.save(user);
        missionProgressRepository.save(missionProgress);
    }

    private void completeMissionIfNeeded(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Mission introductionMission = missionRepository.findById(3)
                .orElseThrow(() -> new RuntimeException("프로필 완성 미션을 찾을 수 없습니다."));

        // 이미 완료된 미션인지 확인
        Optional<MissionProgress> existingProgress = missionProgressRepository
                .findByUserAndMission(user, introductionMission);

        if (existingProgress.isEmpty()) {
            MissionProgress missionProgress = new MissionProgress(user, introductionMission);
            missionProgress.updateProgress(100);
            missionProgress.complete();
            missionProgressRepository.save(missionProgress);
        }
    }


    @Transactional
    public List<MissionDto> getTodaysDailyMissions(Integer userId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 활성화된 모든 일일 미션 조회 (카테고리 'D')
        List<Mission> dailyMissions = missionRepository.findByEnabledTrueAndCategory('D');

        if (dailyMissions.isEmpty()) {
            log.warn("No daily missions found for user: {}", userId);
            return Collections.emptyList();
        }

        // 이미 등록된 미션 확인
        List<MissionProgress> existingMissionProgresses = missionProgressRepository
                .findByUser_UserIdAndMission_CategoryAndCreatedAtBetween(
                        userId, 'D', startOfDay, endOfDay);

        // 기존 미션이 없다면 새로 생성
        if (existingMissionProgresses.isEmpty()) {
            // 기존 당일 미션 삭제
            missionProgressRepository.deleteByUser_UserIdAndMission_Category(userId, 'D');

            // 랜덤 미션 선택 (3개: 출석 미션 포함)
            List<Mission> selectedMissions = new ArrayList<>();

            // 출석 미션 추가
            Mission attendanceMission = missionRepository.findById(2)
                    .orElseThrow(() -> new RuntimeException("Attendance mission not found"));
<<<<<<< HEAD
            selectedMissions.add(attendanceMission);
=======

            MissionProgress attendanceProgress = new MissionProgress(user, attendanceMission);
            attendanceProgress.updateProgress(100);
            attendanceProgress.complete();
            missionProgressRepository.save(attendanceProgress);
>>>>>>> develop

            // 나머지 랜덤 미션 2개 추가 (출석 미션 제외)
            List<Mission> otherDailyMissions = dailyMissions.stream()
                    .filter(m -> m.getMissionId() != 2)  // 출석 미션(id=2) 제외
                    .collect(Collectors.toList());

            Collections.shuffle(otherDailyMissions);
            List<Mission> randomMissions = otherDailyMissions.stream()
                    .limit(2)  // 딱 2개만 선택
                    .collect(Collectors.toList());

            // 랜덤으로 선택된 2개 미션 저장
            for (Mission mission : randomMissions) {
                MissionProgress missionProgress = new MissionProgress(user, mission);
                missionProgress.updateProgress(0);
                missionProgressRepository.save(missionProgress);
            }

            // 업데이트된 미션 리스트 조회
            existingMissionProgresses = missionProgressRepository
                    .findByUser_UserIdAndMission_CategoryAndCreatedAtBetween(
                            userId, 'D', startOfDay, endOfDay);
        }

        // DTO 변환
        return existingMissionProgresses.stream()
                .map(progress -> {
                    Mission mission = progress.getMission();
                    return MissionDto.builder()
                            .missionId(mission.getMissionId())
                            .name(mission.getName())
                            .goal(mission.getGoal())
                            .description(mission.getDescription())
                            .category(mission.getCategory())
                            .coin(mission.getCoin())
                            .ticket(mission.getTicket())
                            .exp(mission.getExp())
                            .isCompleted(progress.getIsCompleted())
                            .rewardClaimed(progress.getRewardClaimed())
                            .build();
                })
                .collect(Collectors.toList());
    }



    // 자동 출석 처리 메서드 (사용자가 페이지 접속 시 호출)
//    @Transactional
//    public void processAutoAttendance(Integer userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Mission attendanceMission = missionRepository.findById(2)
//                .orElseThrow(() -> new RuntimeException("Attendance mission not found"));
//
//        LocalDate today = LocalDate.now();
//        LocalDateTime startOfDay = today.atStartOfDay();
//        LocalDateTime endOfDay = today.atTime(23, 59, 59);
//
//        // 출석 기록 확인 (Attendance 테이블)
//        Optional<Attendance> existingAttendance = attendanceRepository
//                .findByUserAndAttendanceDtBetween(user, startOfDay, endOfDay);
//
//        // Attendance 테이블에 기록이 없으면 추가
//        if (existingAttendance.isEmpty()) {
//            Attendance attendance = new Attendance(user);
//            attendanceRepository.save(attendance);
//        }
//
//        // 기존 MissionProgress 찾아서 업데이트
//        Optional<MissionProgress> existingMissionProgress = missionProgressRepository
//                .findByUserAndMission(user, attendanceMission);
//
//        if (existingMissionProgress.isPresent()) {
//            MissionProgress missionProgress = existingMissionProgress.get();
//            missionProgress.updateProgress(100);
//            missionProgress.complete();
//            missionProgressRepository.save(missionProgress);
//        } else {
//            log.info("Mission progress for attendance does not exist for user: {}", userId);
//        }
//
//    }


    // 출석 여부 확인 메서드
//    public boolean checkTodayAttendance(Integer userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Mission attendanceMission = missionRepository.findById(2)
//                .orElseThrow(() -> new RuntimeException("일일 출석 미션을 찾을 수 없습니다"));
//
//        LocalDate today = LocalDate.now();
//
//        return missionProgressRepository
//                .findByUserAndMissionAndCreatedAtBetween(user, attendanceMission, today.atStartOfDay(), today.atTime(23, 59, 59))
//                .isPresent();
//    }



    // 자기 소개
    @Transactional
    public void updateIntroduction(int userId, String introduction) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Mission introMission = missionRepository.findById(3)
                .orElseThrow(() -> new RuntimeException("매칭 미션을 찾을 수 없습니다"));


        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        MissionProgress missionProgress = missionProgressRepository
                .findByUserAndMissionAndCreatedAtBetween(user, introMission, startOfDay, endOfDay)
                .orElse(null);

        if (missionProgress == null) {
            missionProgress = new MissionProgress(user, introMission);
            missionProgress.updateProgress(100);
            missionProgressRepository.save(missionProgress);
        }
    }


    // 매칭 미션
    public void updateMatchMission(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Mission MatchMission = missionRepository.findById(4)
                .orElseThrow(() -> new RuntimeException("매칭 미션을 찾을 수 없습니다"));

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

<<<<<<< HEAD
        MissionProgress missionProgress = missionProgressRepository
                .findByUserAndMissionAndCreatedAtBetween(user, MatchMission, startOfDay, endOfDay)
                .orElse(null);

        if (missionProgress == null) {
            missionProgress = new MissionProgress(user, MatchMission);
            missionProgress.updateProgress(100);
            missionProgressRepository.save(missionProgress);
        }
    }
=======
    // 스몰톡 참여
    @Transactional
    public void updateSmalltalk(Integer userId) { updateMissionProgress(userId, 13);}

    // 스몰톡 피드백x
    @Transactional
    public void updateSmalltalkFeedback(Integer userId) { updateMissionProgress(userId, 14);}
>>>>>>> develop



}