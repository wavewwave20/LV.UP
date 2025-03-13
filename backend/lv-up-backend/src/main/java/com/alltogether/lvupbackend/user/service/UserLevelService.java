package com.alltogether.lvupbackend.user.service;

import com.alltogether.lvupbackend.user.domain.Level;
import com.alltogether.lvupbackend.user.domain.User;
import com.alltogether.lvupbackend.user.dto.UserLevelInfoDto;
import com.alltogether.lvupbackend.user.repository.LevelRepository;
import com.alltogether.lvupbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserLevelService {
    private final UserRepository userRepository;
    private final LevelRepository levelRepository;


    @Transactional
    public void updateUserExperience(Integer userId, Integer expToAdd) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setTotalExp(user.getTotalExp() + expToAdd);

        List<Level> levels = levelRepository.findAllByOrderByLevelAsc();
        // 현재 누적 경험치로 달성 가능한 최대 레벨 계산
        List<Level> achievableLevels = levels.stream()
                .filter(level -> level.getExpRequired() <= user.getTotalExp())
                .collect(Collectors.toList());

        // 마지막 달성 가능한 레벨 찾기
        if (achievableLevels.isEmpty()) {
            return; // 더 높은 레벨이 없으면 종료
        }

        Level highestAchievableLevel = achievableLevels.get(achievableLevels.size() - 1);

        // 현재 레벨 다음 레벨부터 최대 달성 가능 레벨까지 반복
        for (Level levelToCheck : levels) {
            // 현재 레벨보다 높은 레벨만 처리
            if (levelToCheck.getLevel() > user.getLevel() &&
                    levelToCheck.getLevel() <= highestAchievableLevel.getLevel()) {

                // 코인, 티켓 보상 누적
                user.setCoinQuantity(user.getCoinQuantity() + levelToCheck.getCoin());
                user.setTicketQuantity(user.getTicketQuantity() + levelToCheck.getTicket());

                // 레벨 업데이트
                user.setLevel(levelToCheck.getLevel());
            }

            // 최대 달성 가능 레벨에 도달하면 종료
            if (levelToCheck.getLevel() >= highestAchievableLevel.getLevel()) {
                break;
            }
        }

        // 사용자 정보 저장
        userRepository.save(user);
    }

    private Level calculateUserLevel(Integer totalExp, List<Level> levels) {
        return levels.stream()
                .filter(level -> level.getExpRequired() <= totalExp)
                .max(Comparator.comparing(Level::getExpRequired))
                .orElseThrow(() -> new RuntimeException("Level not found"));
    }

    public UserLevelInfoDto calculateUserLevel(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Integer totalExp = user.getTotalExp();

        List<Level> levels = levelRepository.findAllByOrderByLevelAsc();

        Level currentLevelInfo = levels.stream()
                .filter(level -> level.getExpRequired() <= totalExp)
                .max(Comparator.comparing(Level::getExpRequired))
                .orElseThrow(() -> new RuntimeException("Level not found"));

        // 다음 레벨 정보 (마지막 레벨인 경우 현재 레벨로 처리)
        Level nextLevelInfo = levels.stream()
                .filter(level -> level.getExpRequired() > currentLevelInfo.getExpRequired())
                .findFirst()
                .orElse(currentLevelInfo);

        Integer remainExpToNextLevel = nextLevelInfo.getExpRequired() - totalExp;
        Integer expForNextLevel = nextLevelInfo.getExp();

        return UserLevelInfoDto.builder()
                .currentLevel(currentLevelInfo.getLevel())
                .totalExp(totalExp)
                .currentLevelExp(currentLevelInfo.getExpRequired())
                .nextLevelExp(nextLevelInfo.getExpRequired())
                .remainExpToNextLevel(remainExpToNextLevel)
                .expForNextLevel(expForNextLevel)
                .build();
    }
}