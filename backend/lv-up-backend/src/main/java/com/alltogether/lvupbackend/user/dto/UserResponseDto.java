package com.alltogether.lvupbackend.user.dto;


import com.alltogether.lvupbackend.user.domain.Avatar;
import com.alltogether.lvupbackend.user.domain.Level;
import com.alltogether.lvupbackend.user.domain.User;
import com.alltogether.lvupbackend.user.repository.LevelRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private String userNanoId;
    private String nickname;
    private String introduction;
    private Integer loginType;
    private String email;
    private Character gender;
    private Integer birthyear;
    private Avatar avatar;
    private Integer level;
    private Integer totalExp;
    private Integer coinQuantity;
    private Integer ticketQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer currentLevelExp;
    private Integer nextLevelExp;
    private Integer remainExpToNextLevel;
    private Integer expForNextLevel;

    private Integer role;

    public static UserResponseDto from(User user, LevelRepository levelRepository) {
        List<Level> levels = levelRepository.findAllByOrderByLevelAsc();

        Level currentLevelInfo = levels.stream()
                .filter(level -> level.getExpRequired() <= user.getTotalExp())
                .max(Comparator.comparing(Level::getExpRequired))
                .orElse(levels.get(0));

        // 다음 레벨 정보 (마지막 레벨인 경우 현재 레벨로 처리)
        Level nextLevelInfo = levels.stream()
                .filter(level -> level.getExpRequired() > currentLevelInfo.getExpRequired())
                .findFirst()
                .orElse(currentLevelInfo);

        return UserResponseDto.builder()
                .userNanoId(user.getUserNanoId())
                .nickname(user.getNickname())
                .introduction(user.getIntroduction())
                .loginType(user.getLoginType())
                .email(user.getEmail())
                .gender(user.getGender())
                .birthyear(user.getBirthyear())
                .level(user.getLevel())
                .totalExp(user.getTotalExp())
                .coinQuantity(user.getCoinQuantity())
                .ticketQuantity(user.getTicketQuantity())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .currentLevelExp(currentLevelInfo.getExpRequired())
                .nextLevelExp(nextLevelInfo.getExpRequired())
                .remainExpToNextLevel(nextLevelInfo.getExpRequired() - user.getTotalExp())
                .expForNextLevel(nextLevelInfo.getExp())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .build();
    }
}