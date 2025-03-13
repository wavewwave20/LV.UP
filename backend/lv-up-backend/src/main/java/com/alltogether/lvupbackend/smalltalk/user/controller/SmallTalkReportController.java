package com.alltogether.lvupbackend.smalltalk.user.controller;

import com.alltogether.lvupbackend.smalltalk.user.dto.ReportRequestDto;
import com.alltogether.lvupbackend.smalltalk.user.dto.ReportTypeResponseDto;
import com.alltogether.lvupbackend.smalltalk.user.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/smalltalk/user/report")
@RequiredArgsConstructor
@Slf4j
public class SmallTalkReportController {

    private final ReportService reportService;

    /**
     * 신고 유형 목록 조회
     *
     * GET /api/smalltalk/user/report/options
     *
     * @return 신고 유형 목록 리스트
     */
    @GetMapping("/options")
    public ResponseEntity<?> getReportOptions() {
        List<ReportTypeResponseDto> reportTypes = reportService.getReportOptions();
        return ResponseEntity.ok(reportTypes);
    }

    /**
     * 신고하기 (매칭중에)
     *
     * POST /api/smalltalk/user/report/{sessionId}
     *
     * @param sessionId openvidu 세션 아이디
     * @param reportOptions 신고 요청 정보
     * JSON 형식:
     * {
<<<<<<< HEAD
     *      "userId": 1,
     *      "reportTypeId": [1, 2, 3],
=======
     *      "matchingId": null,
     *      "reportTypeIds": [1, 2, 3],
>>>>>>> develop
     *      "description": "신고 내용"
     * }
     *
     * @return 신고 성공 여부
     */
<<<<<<< HEAD
    @PostMapping("/{sessionId}")
    public ResponseEntity<?> reportUser(@PathVariable("sessionId") Integer sessionId, @RequestBody ReportRequestDto reportOptions) {
        return reportService.reportUser(sessionId, reportOptions);
=======
    @PostMapping("")
    public ResponseEntity<?> reportUser(@RequestBody ReportRequestDto reportOptions) {
        Integer userId = getCurrentUserId();
        return reportService.reportUser(userId, reportOptions);
        //return  reportService.reportAll(userId, reportOptions);
>>>>>>> develop
    }

    /**
     * 신고하기 (매칭 이후)
     *
     * POST /api/smalltalk/user/report/matching
     *
     * @param reportOptions 신고 요청 정보
     * JSON 형식:
     * {
     *      "matchingId": 1,
     *      "userId": 1,
     *      "reportTypeIds": [1, 2, 3],
     *      "description": "신고 내용"
     * }
     *
     * @return 신고 성공 여부
     */
    @PostMapping("/matching")
    public ResponseEntity<?> reportUserAfterMatching(@RequestBody ReportRequestDto reportOptions) {
        return reportService.reportUserAfterMatching(reportOptions);
    }

    @PostMapping("/all")
    public ResponseEntity<?> reportUserPlus(@RequestBody ReportRequestDto reportOptions) {
        Integer userId = getCurrentUserId();
        return reportService.reportAll(userId, reportOptions);
    }
}
