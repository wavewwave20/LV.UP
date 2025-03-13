package com.alltogether.lvupbackend.admin.service;

import com.alltogether.lvupbackend.admin.dto.BlockCategoryDto;
import com.alltogether.lvupbackend.admin.dto.PenaltyRequest;
import com.alltogether.lvupbackend.admin.entity.Block;
import com.alltogether.lvupbackend.admin.entity.BlockBlockCategory;
import com.alltogether.lvupbackend.admin.entity.BlockBlockCategoryId;
import com.alltogether.lvupbackend.admin.entity.BlockCategory;
import com.alltogether.lvupbackend.admin.repository.BlockBlockCategoryRepository;
import com.alltogether.lvupbackend.admin.repository.BlockCategoryRepository;
import com.alltogether.lvupbackend.admin.repository.BlockRepository;
import com.alltogether.lvupbackend.smalltalk.user.dto.ReportDto;
import com.alltogether.lvupbackend.smalltalk.user.dto.ReportTypeDto;
import com.alltogether.lvupbackend.smalltalk.user.entity.Report;
import com.alltogether.lvupbackend.smalltalk.user.entity.ReportReportType;
import com.alltogether.lvupbackend.smalltalk.user.entity.ReportType;
import com.alltogether.lvupbackend.smalltalk.user.repository.ReportReportTypeRepository;
import com.alltogether.lvupbackend.smalltalk.user.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final ReportRepository reportRepository;
    private final ReportReportTypeRepository reportReportTypeRepository;
    private final BlockCategoryRepository blockCategoryRepository;
    private final BlockRepository blockRepository;
    private final BlockBlockCategoryRepository blockBlockCategoryRepository;

    //신고 내역 확인
    public ResponseEntity<?> getReports(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sort));
        Page<Report> reports = reportRepository.findAll(pageable);
        return ResponseEntity.ok(reports.map(ReportDto::from));
    }

    //신고 내용 상세 확인
    public ResponseEntity<?> getReport(Integer reportId) {
        try {
            Report report = reportRepository.findById(reportId).orElseThrow(() -> new IllegalArgumentException("해당 신고 내역이 존재하지 않습니다."));

            List<ReportType> reportTypes = reportReportTypeRepository.findByReport(report)
                    .stream()
                    .map(ReportReportType::getReportType)
                    .toList();

            ReportDto reportDto = ReportDto.from(report);
            reportDto.setReportTypes(reportTypes.stream()
                    .map(ReportTypeDto::from)
                    .toList());

            return ResponseEntity.ok(reportDto);

        } catch (IllegalArgumentException e) {
            log.error("해당 신고 내역이 존재하지 않습니다." + e.getMessage());
            return ResponseEntity.badRequest().body("해당 신고 내역이 존재하지 않습니다.");
        } catch (Exception e) {
            log.error("알 수 없는 오류가 발생했습니다." + e.getMessage());
            return ResponseEntity.badRequest().body("알 수 없는 오류가 발생했습니다.");
        }
    }

    //유저 제재 옵션 조회
    public ResponseEntity<?> getPenaltyOptions() {
        List<BlockCategory> blockCategories = blockCategoryRepository.findAll();
        List<BlockCategoryDto> blockCategoryDtos = blockCategories.stream()
                .map(BlockCategoryDto::from)
                .toList();
        return ResponseEntity.ok(blockCategoryDtos);
    }

    // 유저 제재
    @Transactional
    public ResponseEntity<?> penaltyUser(Integer userId, Integer reportId, PenaltyRequest penaltyRequest) {
        try {
            log.info("PenaltyRequest: " + penaltyRequest);
            //신고 내역 조회
            Report report = reportRepository.findById(reportId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 신고 내역이 존재하지 않습니다."));

            //제재 정보 생성
            Block block = Block.builder()
                    .userId(userId)
                    .endAt(penaltyRequest.getEndAt())
                    .build();

            Block savedBlock = blockRepository.save(block);
            log.info("제재 정보 저장 완료: blockId=" + savedBlock.getBlockId());

            // 제재 매핑 정보 저장
            List<BlockCategory> blockCategories = blockCategoryRepository.findAllById(penaltyRequest.getPenaltyOptions());

            for (BlockCategory category : blockCategories) {
                BlockBlockCategoryId blockBlockCategoryId = new BlockBlockCategoryId(
                            savedBlock.getBlockId(),
                            category.getBlockCategoryId()
                        );

                BlockBlockCategory blockBlockCategory = BlockBlockCategory.builder()
                        .id(blockBlockCategoryId)
                        .block(savedBlock)
                        .blockCategory(category)
                        .build();

                blockBlockCategoryRepository.save(blockBlockCategory);
                log.info("제재 매핑 정보 저장 완료: blockId=" + savedBlock.getBlockId() + ", categoryId=" + category.getBlockCategoryId());

            }

            // 신고 상태 업데이트
            report.updateState("처리됨");
            reportRepository.save(report);

            return ResponseEntity.ok("제재가 완료되었습니다.");

        } catch (IllegalArgumentException e) {
            log.error("해당 신고 내역이 존재하지 않습니다." + e.getMessage());
            return ResponseEntity.badRequest().body("해당 신고 내역이 존재하지 않습니다.");
        } catch (Exception e) {
            log.error("알 수 없는 오류가 발생했습니다." + e.getMessage());
            return ResponseEntity.badRequest().body("알 수 없는 오류가 발생했습니다.");
        }
    }
}
