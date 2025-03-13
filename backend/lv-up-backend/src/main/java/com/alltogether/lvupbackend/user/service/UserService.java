package com.alltogether.lvupbackend.user.service;


import com.alltogether.lvupbackend.login.util.JwtTokenProvider;
import com.alltogether.lvupbackend.mission.repository.MissionProgressRepository;
import com.alltogether.lvupbackend.mission.repository.MissionRepository;
import com.alltogether.lvupbackend.mission.service.MissionService;
import com.alltogether.lvupbackend.user.domain.*;
import com.alltogether.lvupbackend.user.dto.*;
import com.alltogether.lvupbackend.user.repository.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final InterestRepository interestRepository;
    private final AvatarRepository avatarRepository;
    private final AvatarOwnRepository avatarOwnRepository;

    // 출석
    private final MissionService missionService;

    // 미션
    private final MissionRepository missionRepository;
    private final MissionProgressRepository missionProgressRepository;

    private final LevelRepository levelRepository;

    // 회원탈퇴
    @Transactional
    public void withdrawUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        user.setEnabled(false);
        userRepository.save(user);
    }

    // 관심사
    public List<InterestResponseDto> getInterestOptions() {
        return interestRepository.findAll().stream()
                .map(InterestResponseDto::from)
                .collect(Collectors.toList());
    }

    public List<String> getUserInterests(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return user.getInterests().stream()
                .map(Interest::getName)
                .collect(Collectors.toList());
    }
    public void selectUserInterests(Integer userId, List<Integer> interestIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 사용자의 관심사 선택
        List<Interest> interests = interestRepository.findAllById(interestIds);

        // 기존 관심사 초기화 후 새로운 관심사 추가
        user.getInterests().clear();
        user.getInterests().addAll(interests);

        userRepository.save(user);
    }

    public List<AvatarResponseDto> getAvatarList() {
        return avatarRepository.findAll().stream()
                .map(AvatarResponseDto::from)
                .collect(Collectors.toList());
    }

    // 사용자의 보유 아바타 목록 조회
    public List<AvatarResponseDto> getUserAvatarList(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 사용자의 보유 아바타 로직 (avatar_own 테이블 기반)
        return avatarRepository.findAvatarsByUser(userId).stream()
                .map(AvatarResponseDto::from)
                .collect(Collectors.toList());
    }
    // 선택된 아바타 ID 조회
    public Integer getSelectedAvatarId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return user.getAvatar() != null ? user.getAvatar().getAvatarId() : null;
    }

    // 회원가입 시 기본 아바타 추가
    @Transactional
    public void addInitialAvatars(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        List<Avatar> avatars = avatarRepository.findAll();
        List<AvatarOwn> avatarOwns = new ArrayList<>();

        for (Avatar avatar : avatars) {
            AvatarOwnId avatarOwnId = new AvatarOwnId(avatar.getAvatarId(), userId);
            AvatarOwn avatarOwn = AvatarOwn.builder()
                    .id(avatarOwnId)
                    .avatar(avatar)
                    .user(user)
                    .build();

            avatarOwns.add(avatarOwn);
        }

        avatarOwnRepository.saveAll(avatarOwns);

        // 첫 번째 아바타를 기본 선택 아바타로 설정
        if (!avatars.isEmpty()) {
            user.setAvatar(avatars.get(0));
            userRepository.save(user);
        }
    }

    // 사용자의 대표 아바타 변경
    public void selectMainAvatar(Integer userId, Integer avatarId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 해당 아바타가 존재하는지 확인
        Avatar avatar = avatarRepository.findById(avatarId)
                .orElseThrow(() -> new RuntimeException("아바타를 찾을 수 없습니다."));

        // 사용자가 해당 아바타를 보유하고 있는지 확인
        AvatarOwnId avatarOwnId = new AvatarOwnId(avatarId, userId);
        boolean hasAvatar = avatarOwnRepository.existsById(avatarOwnId);

        if (!hasAvatar) {
            throw new RuntimeException("보유하지 않은 아바타입니다.");
        }

        // 대표 아바타 변경
        user.setAvatar(avatar);
        userRepository.save(user);
    }


    public Integer getCoinAmount(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return user.getCoinQuantity();
    }

    public Integer getTicketAmount(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return user.getTicketQuantity();
    }

    // Main 페이지 정보 조회
    @Transactional(readOnly = true)
    public UserResponseDto getMainInfo(Integer userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 자동 출석 처리
//        missionService.processAutoAttendance(userId);

        return UserResponseDto.from(user, levelRepository);
    }

    // User 페이지 정보 조회
    @Transactional
    public UserInfoDto getUserInfo(Integer userId) {
        log.info("Getting user info and processing auto attendance for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));


        // 자동 출석 처리
//        missionService.processAutoAttendance(userId);

        List<String> userInterests = user.getInterests().stream()
                .map(Interest::getName)
                .collect(Collectors.toList());


        return UserInfoDto.builder()
                .nickname(user.getNickname())
                .birthyear(user.getBirthyear())
                .interests(userInterests)
                .introduction(user.getIntroduction())
                .build();
    }

    public boolean checkAndUpdateNickname(int userId, String nickname) {
        // 닉네임 중복 확인
        if (userRepository.existsByNickname(nickname)) {
            return false; // 닉네임이 이미 존재하면 false 반환
        }

        // 중복이 아니면 닉네임 업데이트
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        user.setNickname(nickname);
        userRepository.save(user);

        return true; // 변경 성공 시 true 반환
    }

<<<<<<< HEAD
//     닉네임 중복확인
//    public boolean existsByNickName(String nickname){
//        return userRepository.existsByNickname(nickname);
//    }
//    //닉네임
//    public void updateNickname(int userId, String nickname) {
//        User user = userRepository.findByUserId(userId)
//                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
//        user.setNickname(nickname);
//        userRepository.save(user);
//    }
=======
    // 닉네임 중복 확인
    public boolean checkNickname(int userId, String nickname) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 본인의 닉네임이면 중복 아님
        if (user.getNickname().equals(nickname)) {
            return true;
        }

        // 다른 사람이 이미 사용 중인 닉네임이면 false
        return !userRepository.existsByNickname(nickname);
    }

>>>>>>> develop

    public void updateIntroduction(int userId, String introduction) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        user.setIntroduction(introduction);
        userRepository.save(user);
        // 미션 완료 처리 추가
        completeMissionIfNeeded(userId);
    }

    private void completeMissionIfNeeded(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Mission introductionMission = missionRepository.findById(3)
                .orElseThrow(() -> new RuntimeException("프로필 완성 미션을 찾을 수 없습니다."));

        Optional<MissionProgress> existingProgress = missionProgressRepository
                .findByUserAndMission(user, introductionMission);

        if (existingProgress.isEmpty()) {
            MissionProgress missionProgress = new MissionProgress(user, introductionMission);
            missionProgress.updateProgress(100.0);
            missionProgressRepository.save(missionProgress);
        }
    }


    public User findUser(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    // regiester 최종
    public void completeRegister(Integer userId, RegisterCompleteDto registerDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        List<Interest> interests = interestRepository.findAllById(registerDto.getInterest());
        user.getInterests().clear();
        user.getInterests().addAll(interests);

        Avatar avatar = avatarRepository.findById(registerDto.getAvatar_id())
                .orElseThrow(() -> new RuntimeException("아바타를 찾을 수 없습니다."));

        AvatarOwnId avatarOwnId = new AvatarOwnId(avatar.getAvatarId(), userId);
        if (!avatarOwnRepository.existsById(avatarOwnId)) {
            AvatarOwn avatarOwn = AvatarOwn.builder()
                    .id(avatarOwnId)
                    .avatar(avatar)
                    .user(user)
                    .build();
            avatarOwnRepository.save(avatarOwn);
        }

        user.setAvatar(avatar);

//        user.setRegisterCompleted(true);
        if (!interests.isEmpty() && avatar != null) {
            user.setRegisterCompleted(true);
<<<<<<< HEAD
=======

//            missionService.getTodaysDailyMissions(userId);
>>>>>>> develop
        }

        userRepository.save(user);
    }

    // 관리자인지
    public boolean isAdmin(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return user.getRole() == 1;  // role이 1이면 true, 아니면 false 반환
    }


}