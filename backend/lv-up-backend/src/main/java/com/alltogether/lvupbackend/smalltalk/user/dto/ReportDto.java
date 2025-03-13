package com.alltogether.lvupbackend.smalltalk.user.dto;

import com.alltogether.lvupbackend.smalltalk.user.entity.Report;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
    private Integer reportId;
    private Integer reporter;
    private Integer reportee;
    private String state;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<ReportTypeDto> reportTypes;

    public static ReportDto from(Report report) {
        return ReportDto.builder()
                .reportId(report.getReportId())
                .reporter(report.getReporter().getUserId())
                .reportee(report.getReportee().getUserId())
                .state(report.getState())
                .description(report.getDescription())
                .createdAt(report.getCreatedAt())
                .updatedAt(report.getUpdatedAt())
                .build();
    }

}
