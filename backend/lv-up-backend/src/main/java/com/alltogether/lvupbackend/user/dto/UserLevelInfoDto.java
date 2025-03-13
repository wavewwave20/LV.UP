package com.alltogether.lvupbackend.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLevelInfoDto {
    private Integer currentLevel;      // 현재 레벨
    private Integer totalExp;           // 전체 누적 경험치
    private Integer currentLevelExp;    // 현재 레벨의 시작 누적 경험치
    private Integer nextLevelExp;       // 다음 레벨의 누적 경험치
    private Integer remainExpToNextLevel; // 다음 레벨까지 남은 경험치
    private Integer expForNextLevel;    // 다음 레벨로 가기 위해 필요한 경험치
}