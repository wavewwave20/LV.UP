package com.alltogether.lvupbackend.smalltalk.checklist.dto;

import com.alltogether.lvupbackend.smalltalk.checklist.entity.ChecklistMaster;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistMasterResponseDto {
    private Integer checklistMasterId;
    private String name;
    private Float score;
    private String checker;

    // ID와 name만을 위한 생성자 추가
//    public ChecklistMasterResponseDto(Integer checklistMasterId, String name) {
//        this.checklistMasterId = checklistMasterId;
//        this.name = name;
//    }

    // 전체 정보 변환 메서드
    public static ChecklistMasterResponseDto from(ChecklistMaster entity) {
        return new ChecklistMasterResponseDto(
                entity.getChecklistMasterId(),
                entity.getName(),
                entity.getScore(),
                entity.getChecker()
        );
    }

    // ID와 name만 변환하는 메서드
    public static ChecklistMasterResponseDto fromSimple(ChecklistMaster entity) {
        return new ChecklistMasterResponseDto(
                entity.getChecklistMasterId(),
                entity.getName(),
                null,
                null
        );
    }
}