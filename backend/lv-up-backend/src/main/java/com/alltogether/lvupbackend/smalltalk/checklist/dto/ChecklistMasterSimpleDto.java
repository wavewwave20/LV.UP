package com.alltogether.lvupbackend.smalltalk.checklist.dto;

import com.alltogether.lvupbackend.smalltalk.checklist.entity.ChecklistMaster;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistMasterSimpleDto {
    private Integer checklistMasterId;
    private String name;

    public static ChecklistMasterSimpleDto from(ChecklistMaster entity) {
        return new ChecklistMasterSimpleDto(
                entity.getChecklistMasterId(),
                entity.getName()
        );
    }
}