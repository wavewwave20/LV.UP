package com.alltogether.lvupbackend.smalltalk.user.service;

import com.alltogether.lvupbackend.smalltalk.user.dto.ReportRequestDto;
import com.alltogether.lvupbackend.smalltalk.user.dto.ReportTypeResponseDto;
import com.alltogether.lvupbackend.smalltalk.user.entity.Report;
import com.alltogether.lvupbackend.smalltalk.user.entity.ReportReportType;
import com.alltogether.lvupbackend.smalltalk.user.entity.ReportType;
import com.alltogether.lvupbackend.smalltalk.user.repository.MatchingUserRepository;
import com.alltogether.lvupbackend.smalltalk.user.repository.ReportReportTypeRepository;
import com.alltogether.lvupbackend.smalltalk.user.repository.ReportRepository;
import com.alltogether.lvupbackend.smalltalk.user.repository.ReportTypeRepository;
import com.alltogether.lvupbackend.user.domain.Blacklist;
import com.alltogether.lvupbackend.user.domain.User;
import com.alltogether.lvupbackend.user.repository.BlacklistRepository;
import com.alltogether.lvupbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final ReportTypeRepository reportTypeRepository;
    private final SessionService sessionService;
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final ReportReportTypeRepository reportReportTypeRepository;
    private final MatchingUserRepository matchingUserRepository;
    private final BlacklistRepository blacklistRepository;
    private final MatchService matchService;

    /**
     * 신고 유형 목록 조회
     *
     * @return 신고 유형 목록 리스트
     */
    public List<ReportTypeResponseDto> getReportOptions() {
        List<ReportType> reportTypes = reportTypeRepository.findAll();
        return reportTypes.stream()
                .map(type-> ReportTypeResponseDto.builder()
                        .reportTypeId(type.getReportTypeId())
                        .description(type.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 신고하기
     *
     * @param sessionId openvidu 세션 아이디
     * @param reportOptions 신고 요청 정보
     * @return 신고 성공 여부
     */
    @Transactional
    public ResponseEntity<?> reportUser(Integer sessionId, ReportRequestDto reportOptions) {
        try {
            // 현재 유저가 속한 세션 검증
            if (sessionService.isUserValidSession(sessionId, reportOptions.getUserId())) {
                log.error("존재하지 않는 세션: " + sessionId);
                return ResponseEntity.badRequest().body("존재하지 않는 세션입니다.");
            }

<<<<<<< HEAD
=======
            List<String> sessionUsers = sessionInfo.getValue();
            if (sessionUsers.size() < 2) {
                log.error("현재 유저가 속한 세션 정보가 올바르지 않습니다. userId: {}", userId);
                return ResponseEntity.badRequest().body("유효하지 않은 세션입니다.");
            }

            // 2. 매칭 유저 테이블에서 해당 유저의 최신 데이터 조회
            MatchingUser latestMatch = matchingUserRepository.findTopByUserIdOrderByMatchingUserIdDesc(userId);
            if (latestMatch == null) {
                log.error("매칭 정보를 찾을 수 없습니다. userId: {}", userId);
                return ResponseEntity.badRequest().body("매칭 정보를 찾을 수 없습니다.");
            }

            // 3. 유저가 속한 세션 정보의 상대방 아이디와 2에서 가져온 최신 데이터의 상대방 아이디를 확인
            String otherUserId = sessionUsers.stream()
                    .filter(id -> !id.equals(userId.toString()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("상대방 사용자를 찾을 수 없습니다."));

            if (!otherUserId.equals(latestMatch.getRateeId().toString())) {
                return ResponseEntity.badRequest().body("매칭된 사용자 정보가 일치하지 않습니다.");
            }

            // 4. 신고 처리 시작
            log.info("신고 처리 시작: matchingUserId: {}, matchingId: {}, userId: {}, otherUserId: {}",
                    latestMatch.getMatchingUserId(), latestMatch.getMatchingId().getMatchingId(), userId, otherUserId);
            ResponseEntity<?> response = processReport(latestMatch, userId, Integer.parseInt(otherUserId),
                    reportOptions);

            if (response.getStatusCode().is2xxSuccessful()) {
                matchService.endMatch(userId);
                return ResponseEntity.ok("신고가 접수되었습니다");
            } else {
                return response;
            }

>>>>>>> develop
            // 신고 대상 유저 조회
            String report_target = sessionService.findOtherUserInSession(sessionId, reportOptions.getUserId());

            if (report_target == null) {
                log.error("신고 대상 유저가 없습니다.");
                return ResponseEntity.badRequest().body("신고 대상 유저가 없습니다.");
            }

            // 신고 처리
            return processReport(reportOptions.getUserId(), Integer.parseInt(report_target), reportOptions);
        } catch (Exception e) {
            log.error("신고 처리 중 오류: " + e.getMessage());
            return ResponseEntity.badRequest().body("신고 처리 중 오류가 발생했습니다.");
        }
    }

    /**
     * 신고하기 (매칭 이후)
     *
     * @param reportOptions 신고 요청 정보
     * JSON 형식:
     * {
     *      "matchingId": 1,
     *      "reportTypeIds": [1, 2, 3],
     *      "description": "신고 내용"
     * }
     * @return 신고 성공 여부
     */
    @Transactional
    public ResponseEntity<?> reportUserAfterMatching(ReportRequestDto reportOptions) {
        // 신고 정보 저장
        try {
            log.info("신고 요청 받음: {}", reportOptions);

            // 매칭 아이디를 통한 신고자들 조회
            List<Integer> matchingUserIds = matchingUserRepository.findUserIdsByMatchingId(reportOptions.getMatchingId());
            log.info("매칭 아이디 {}를 통한 신고자들 조회: {}", reportOptions.getMatchingId(), matchingUserIds);

            List<Integer> matchingUserId = matchingUserIds
                    .stream()
                    .filter(userId -> !userId.equals(reportOptions.getUserId()))
                    .toList();

            if (matchingUserId.isEmpty()) {
                log.error("매칭 정보가 존재하지 않습니다.");
                return ResponseEntity.badRequest().body("매칭 정보가 존재하지 않습니다.");
            }

            if (matchingUserId.get(0).equals(reportOptions.getUserId())) {
                log.info("신고자와 피신고자가 같습니다");
                return ResponseEntity.badRequest().body("자신을 신고할 수 없습니다.");
            }


            // 신고 처리
            return processReport(reportOptions.getUserId(), matchingUserId.get(0), reportOptions);

        } catch (Exception e) {
            log.error("신고 처리 중 오류: " + e.getMessage());
            return ResponseEntity.badRequest().body("신고 처리 중 오류가 발생했습니다.");
        }
    }

<<<<<<< HEAD
    private ResponseEntity<?> processReport(Integer reporterId, Integer reporteeId, ReportRequestDto reportOptions) {
=======
    @Transactional
    public ResponseEntity<?> reportAll(Integer userId, ReportRequestDto request) {
        log.info("신고 요청 받음: userId: {}, options: {}", userId, request.toString());

        try {
            MatchingUser matchingUser;
            Integer reporteeId;

            if (request.getMatchingId() == null) {
                // 매칭 중 신고 처리
                log.info("매칭 중 신고");
                Map.Entry<String, List<String>> sessionInfo = sessionService.getSessionByUserId(userId);
                if (sessionInfo == null) {
                    return ResponseEntity.badRequest().body("세션을 찾을 수 없습니다.");
                }

                List<String> sessionUsers = sessionInfo.getValue();
                if (sessionUsers.size() < 2) {
                    return ResponseEntity.badRequest().body("유효하지 않은 세션입니다.");
                }

                matchingUser = matchingUserRepository.findTopByUserIdOrderByMatchingUserIdDesc(userId);
                if (matchingUser == null) {
                    return ResponseEntity.badRequest().body("매칭 정보를 찾을 수 없습니다.");
                }

                String otherUserId = sessionUsers.stream()
                        .filter(id -> !id.equals(userId.toString()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("상대방 사용자를 찾을 수 없습니다."));

                if (!otherUserId.equals(matchingUser.getRateeId().toString())) {
                    return ResponseEntity.badRequest().body("매칭된 사용자 정보가 일치하지 않습니다.");
                }

                reporteeId = Integer.parseInt(otherUserId);
            } else {
                // 매칭 후 신고 처리
                log.info("매칭 후 신고 처리");
                matchingUser = matchingUserRepository
                        .findByMatchingId_MatchingIdAndUserId(request.getMatchingId(), userId);
                if (matchingUser == null) {
                    return ResponseEntity.badRequest().body("매칭 정보를 찾을 수 없습니다.");
                }

                if (!matchingUserRepository.existsByMatchingId_MatchingIdAndUserId(
                        request.getMatchingId(), matchingUser.getRateeId())) {
                    return ResponseEntity.badRequest().body("피매칭자의 매칭 정보를 찾을 수 없습니다.");
                }

                reporteeId = matchingUser.getRateeId();
            }

            ResponseEntity<?> response = processReport(matchingUser, userId, reporteeId, request);

            // 매칭 중 신고인 경우에만 매칭 종료
            if (response.getStatusCode().is2xxSuccessful() && request.getMatchingId() == null) {
                log.info("매칭 종료 요청");
                matchService.endMatch(userId);
            }

            return response;

        } catch (Exception e) {
            log.error("신고 처리 중 오류: " + e.getMessage());
            return ResponseEntity.badRequest().body("신고 처리 중 오류가 발생했습니다.");
        }
    }

    @Transactional
    protected ResponseEntity<?> processReport(MatchingUser matchingUser, Integer reporterId, Integer reporteeId,
            ReportRequestDto reportOptions) {
>>>>>>> develop
        // 신고 정보 저장
        try {
            //신고자와 피신고자 정보 조회
            User reporter = userRepository.findByUserId(reporterId)
                    .orElseThrow(() -> new IllegalArgumentException("신고자가 존재하지 않습니다."));

            User reportee = userRepository.findByUserId(reporteeId)
                    .orElseThrow(() -> new IllegalArgumentException("피신고자가 존재하지 않습니다."));

            //신고 정보 저장
            Report report = Report.builder()
                    .reporter(reporter)
                    .reportee(reportee)
                    .state("대기중")
                    .description(reportOptions.getDescription())
                    .build();

            Report savedReport = reportRepository.save(report);
            log.info("신고 정보 저장 완료: {}", savedReport.getReportId());

            // 매핑 정보 저장
            List<ReportType> reportTypes = reportTypeRepository.findAllById(reportOptions.getReportTypeIds());
            if (reportTypes.isEmpty()) {
                log.error("신고 유형이 존재하지 않습니다.");
                throw new IllegalArgumentException("신고 유형이 존재하지 않습니다.");
            }

            for (ReportType reportType : reportTypes) {
                ReportReportType.ReportReportTypeId reportReportTypeId =
                        new ReportReportType.ReportReportTypeId(
                                savedReport.getReportId(),
                                reportType.getReportTypeId()
                        );

                ReportReportType reportReportType = ReportReportType.builder()
                                .id(reportReportTypeId)
                                .report(savedReport)
                                .reportType(reportType)
                                .build();

                reportReportTypeRepository.save(reportReportType);
                log.info("신고 유형 매핑 저장 완료: reportId: {}, reportTypeId={}", reportReportType.getId(), reportType.getReportTypeId());
            }

            log.info("신고가 접수되었습니다. 신고자: " + reporter.getUserId() + ", 피신고자: " + reportee.getUserId());

            // 유저 차단
            Blacklist blacklist = Blacklist.create(reporter, reportee);
            Blacklist savedBlacklist = blacklistRepository.save(blacklist);
<<<<<<< HEAD
            log.info("유저 차단 완료: blacker: " + savedBlacklist.getBlacker().getUserId() + ", blackee: " + savedBlacklist.getBlackee().getUserId());
=======
            
            // TODO: 각 유저가 누굴 차단했는지 명확히 할거면 DB 수정하거나 이부분을 비활성화
//            Blacklist blacklist2 = Blacklist.create(reportee, reporter);
//            Blacklist savedBlacklist2 = blacklistRepository.save(blacklist2);
            
            log.info("유저 차단 완료: blacker: " + savedBlacklist.getBlacker().getUserId() + ", blackee: "
                    + savedBlacklist.getBlackee().getUserId());
>>>>>>> develop

            return ResponseEntity.ok("신고가 접수되었습니다");

        } catch (IllegalArgumentException e) {
            log.error("신고 처리 중 유효성 검증 오류: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (DataIntegrityViolationException e) {
            log.error("데이터베이스 제약조건 위반: {}", e.getMessage());
            return ResponseEntity.badRequest().body("데이터 저장 중 오류가 발생했습니다.");

        } catch (Exception e) {
            log.error("신고 처리 중 오류: " + e.getMessage());
            return ResponseEntity.badRequest().body("신고 처리 중 오류가 발생했습니다.");
        }
    }
}
