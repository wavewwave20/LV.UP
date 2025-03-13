package com.alltogether.lvupbackend.smalltalk.user.repository;

import com.alltogether.lvupbackend.smalltalk.user.entity.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportTypeRepository extends JpaRepository<ReportType, Integer> {
}
