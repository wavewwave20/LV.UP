package com.alltogether.lvupbackend.smalltalk.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchingUserWithRateeDTO {
    private Integer matchingUserId;
    private Byte ratingScore;
    private LocalDateTime startAt;
    private String rateeName;
}
