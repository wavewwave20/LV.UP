package com.alltogether.lvupbackend.smalltalk.checklist.service;

import com.alltogether.lvupbackend.smalltalk.checklist.dto.ChecklistMasterResponseDto;
import com.alltogether.lvupbackend.smalltalk.checklist.repository.ChecklistMasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChecklistService {
    
    private final ChecklistMasterRepository checklistMasterRepository;
    
    // 모든 체크리스트 마스터 조회
    public List<ChecklistMasterResponseDto> getAllChecklists() {
        return checklistMasterRepository.findAll()
            .stream()
            .map(ChecklistMasterResponseDto::from)
            .collect(Collectors.toList());
    }
    
    // AI용 체크리스트만 조회
    public List<ChecklistMasterResponseDto> getAIChecklists() {
        return checklistMasterRepository.findAllByChecker("A")
            .stream()
            .map(ChecklistMasterResponseDto::from)
            .collect(Collectors.toList());
    }
    
    // 사용자용 체크리스트만 조회
    public List<ChecklistMasterResponseDto> getUserChecklists() {
        return checklistMasterRepository.findAllByChecker("U")
            .stream()
            .map(ChecklistMasterResponseDto::from)
            .collect(Collectors.toList());
    }
}