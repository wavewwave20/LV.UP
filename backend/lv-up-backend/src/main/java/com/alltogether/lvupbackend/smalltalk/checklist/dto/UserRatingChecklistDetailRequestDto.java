package com.alltogether.lvupbackend.smalltalk.checklist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRatingChecklistDetailRequestDto {
    private Integer matchingUserId;
    private Integer checklistMasterId;
}