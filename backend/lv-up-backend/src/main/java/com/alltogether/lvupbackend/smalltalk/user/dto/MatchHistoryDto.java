package com.alltogether.lvupbackend.smalltalk.user.dto;

import java.time.LocalDateTime;

import com.alltogether.lvupbackend.smalltalk.user.entity.Matching;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MatchHistoryDto {
    private Integer matchingId;
    private Integer matchingModeId;
    private String state;
    private Integer interestId;
    private LocalDateTime createdAt;
    
    public static MatchHistoryDto from(Matching matching) {
        return MatchHistoryDto.builder()
                .matchingId(matching.getMatchingId())
                .matchingModeId(matching.getMatchingModeId())
                .state(matching.getState())
                .interestId(matching.getInterestId())
                .createdAt(matching.getCreatedAt())
                .build();
    }
}