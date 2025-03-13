package com.alltogether.lvupbackend.smalltalk.user.repository;

import com.alltogether.lvupbackend.smalltalk.user.entity.Report;
import com.alltogether.lvupbackend.smalltalk.user.entity.ReportReportType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportReportTypeRepository extends JpaRepository<ReportReportType, Integer> {
    List<ReportReportType> findByReport(Report report);
}
