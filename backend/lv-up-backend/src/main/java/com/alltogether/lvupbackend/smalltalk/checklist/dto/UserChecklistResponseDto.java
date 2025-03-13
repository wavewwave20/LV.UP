package com.alltogether.lvupbackend.smalltalk.checklist.dto;

import com.alltogether.lvupbackend.smalltalk.checklist.entity.ChecklistMaster;
import com.alltogether.lvupbackend.smalltalk.checklist.entity.UserRatingChecklistDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserChecklistResponseDto {
    private List<String> checklistNames;
    private String rateeNickname;
    private Byte ratingScore;
    private String ratingContent;
    private Integer rateeAvatarId;
    private LocalDateTime startAt;
}
