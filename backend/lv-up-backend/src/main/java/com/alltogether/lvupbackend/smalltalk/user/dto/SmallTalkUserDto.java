package com.alltogether.lvupbackend.smalltalk.user.dto;

import java.time.LocalDateTime;

import com.alltogether.lvupbackend.smalltalk.user.entity.MatchingUser;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SmallTalkUserDto {

    // 요청
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RequestMatchHistory {
        private Integer matchingModeId;
        private String state;
        private Integer interestId;
        private String dialogue;
    }

    // 응답 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Integer matchingUserId;
        private Integer userId;
        private Integer userAvatar;
        private Boolean extend;
        private Boolean accept;
        private Boolean report;
        private Byte ratingScore;
        private String ratingContent;
        private Integer rateeId;
        private LocalDateTime startAt;
        private LocalDateTime endAt;

        public static Response from(MatchingUser entity) {
            return Response.builder()
                    .matchingUserId(entity.getMatchingUserId())
                    .userId(entity.getUserId())
                    .userAvatar(entity.getUserAvatar())
                    .extend(entity.getExtend())
                    .accept(entity.getAccept())
                    .report(entity.getReport())
                    .ratingScore(entity.getRatingScore())
                    .ratingContent(entity.getRatingContent())
                    .rateeId(entity.getRateeId())
                    .startAt(entity.getStartAt())
                    .endAt(entity.getEndAt())
                    .build();
        }
    }

}