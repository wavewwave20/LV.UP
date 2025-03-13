package com.alltogether.lvupbackend.smalltalk.user.repository;

import com.alltogether.lvupbackend.smalltalk.user.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Integer> {

    Page<Report> findAll(Pageable pageable);
}
