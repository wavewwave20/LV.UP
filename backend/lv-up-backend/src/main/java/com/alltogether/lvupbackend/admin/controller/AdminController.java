package com.alltogether.lvupbackend.admin.controller;

import com.alltogether.lvupbackend.admin.dto.PenaltyRequest;
import com.alltogether.lvupbackend.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<?> checkAdmin() {
        return ResponseEntity.ok("어드민 확인");
    }

    /**
     * 관리자가 신고 내역을 확인
     *
     * GET /api/admin/reports?page=1&size=10&sort=desc
     *
     * @param page 페이지 번호
     * @param size 페이지 사이즈
     * @param sort 정렬 기준
     *             - desc: 내림차순
     *             - asc: 오름차순
     * @return 신고 내역 리스트
     */
    @GetMapping("/reports")
    public ResponseEntity<?> getReports(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false, defaultValue = "createdAt") String sort) {
        return adminService.getReports(page, size, sort);
    }

    /**
     * 신고 내용 상세 확인
     *
     * GET /api/admin/report/{reportId}
     *
     * @param reportId 신고 아이디
     * @return 신고 내용
     */
    @GetMapping("/report/{reportId}")
    public ResponseEntity<?> getReport(@PathVariable Integer reportId) {
        return adminService.getReport(reportId);
    }

    /**
     * 유저 제재 옵션 조회
     *
     * GET /api/admin/penalty/options
     *
     * @return 제재 옵션 리스트
     */
    @GetMapping("/penalty/options")
    public ResponseEntity<?> getPenaltyOptions() {
        return adminService.getPenaltyOptions();
    }

    /**
     * 유저 제재
     *
     * POST /api/admin/penalty?userId=1&reportId=1
     *
     * @param userId   제재할 유저 아이디
     * @param reportId 신고 내역 아이디
     * @body
     *       {
     *       "penaltyOptions": [1, 2, 3],
     *       "endAt": "2030-12-31T23:59:59"
     *       }
     * @return 제재 결과
     */
    @PostMapping("/penalty")
    public ResponseEntity<?> penaltyUser(@RequestParam Integer userId, @RequestParam Integer reportId,
            @RequestBody PenaltyRequest penaltyRequest) {
        return adminService.penaltyUser(userId, reportId, penaltyRequest);
    }

}
