package com.alltogether.lvupbackend.smalltalk.user.dto;

import com.alltogether.lvupbackend.smalltalk.user.entity.MatchingUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingUserResponseDto {
    private Integer matchingUserId;
    private Integer userId;
    private Byte ratingScore;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public static MatchingUserResponseDto from(MatchingUser matchingUser) {
        return MatchingUserResponseDto.builder()
                .matchingUserId(matchingUser.getMatchingUserId())
                .userId(matchingUser.getUserId())
                .ratingScore(matchingUser.getRatingScore())
                .startAt(matchingUser.getStartAt())
                .endAt(matchingUser.getEndAt())
                .build();
    }
}
