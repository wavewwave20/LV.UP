package com.alltogether.lvupbackend.smalltalk.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "report_report_type")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportReportType {

    @Embeddable
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportReportTypeId implements Serializable {
        private Integer reportId;
        private Integer reportTypeId;
    }

    @EmbeddedId
    private ReportReportTypeId id;

    @ManyToOne
    @MapsId("reportId")
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @ManyToOne
    @MapsId("reportTypeId")
    @JoinColumn(name = "report_type_id", nullable = false)
    private ReportType reportType;
}
