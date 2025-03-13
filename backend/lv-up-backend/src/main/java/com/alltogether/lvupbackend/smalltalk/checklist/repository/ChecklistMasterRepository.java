package com.alltogether.lvupbackend.smalltalk.checklist.repository;

import com.alltogether.lvupbackend.smalltalk.checklist.entity.ChecklistMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChecklistMasterRepository extends JpaRepository<ChecklistMaster, Integer> {

    // 모든 체크리스트 마스터 조회
    List<ChecklistMaster> findAll();

    // checker 타입별 체크리스트 조회 (AI용 또는 사용자용)
    @Query("SELECT c FROM ChecklistMaster c WHERE c.checker = :checker")
    List<ChecklistMaster> findAllByChecker(@Param("checker") String checker);

    // ID로 특정 체크리스트 마스터 조회
    ChecklistMaster findByChecklistMasterId(Integer checklistMasterId);
}