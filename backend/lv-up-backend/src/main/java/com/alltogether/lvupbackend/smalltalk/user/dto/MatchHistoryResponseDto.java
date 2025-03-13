package com.alltogether.lvupbackend.smalltalk.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MatchHistoryResponseDto {
    private Integer matchingId;
    private String partnerName;
    private LocalDateTime startAt;
    private Byte ratingScore;
    private String modeName;
    private Byte reported;

    public MatchHistoryResponseDto(
        Integer matchingId,
        String partnerName,
        LocalDateTime startAt,
        Byte ratingScore,
        String modeName,
        Byte reported
    ) {
        this.matchingId = matchingId;
        this.partnerName = partnerName;
        this.startAt = startAt;
        this.ratingScore = ratingScore;
        this.modeName = modeName;
        this.reported = reported;
    }
}