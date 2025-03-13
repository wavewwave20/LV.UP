package com.alltogether.lvupbackend.smalltalk.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportReportTypeDto {

    private Integer reportId;
    private Integer reportTypeId;
}
