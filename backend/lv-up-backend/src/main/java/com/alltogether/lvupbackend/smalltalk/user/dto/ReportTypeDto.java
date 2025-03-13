package com.alltogether.lvupbackend.smalltalk.user.dto;

import com.alltogether.lvupbackend.smalltalk.user.entity.ReportType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ReportTypeDto {

    private Integer reportTypeId;
    private String description;

    public static ReportTypeDto from(ReportType reportType) {
        return ReportTypeDto.builder()
                .reportTypeId(reportType.getReportTypeId())
                .description(reportType.getDescription())
                .build();
    }
}
