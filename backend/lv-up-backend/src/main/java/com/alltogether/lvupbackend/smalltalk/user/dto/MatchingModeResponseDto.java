package com.alltogether.lvupbackend.smalltalk.user.dto;

import com.alltogether.lvupbackend.smalltalk.user.entity.MatchingMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingModeResponseDto {
    private Integer matchingModeId;
    private String content;
    private String description;

    public static MatchingModeResponseDto from(MatchingMode matchingMode) {
        return MatchingModeResponseDto.builder()
                .matchingModeId(matchingMode.getMatchingModeId())
                .content(matchingMode.getContent())
                .description(matchingMode.getDescription())
                .build();
    }
}
