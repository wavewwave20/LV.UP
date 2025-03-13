package com.alltogether.lvupbackend.smalltalk.user.service;

import com.alltogether.lvupbackend.smalltalk.checklist.dto.ChecklistMasterSimpleDto;
import com.alltogether.lvupbackend.smalltalk.checklist.dto.UserChecklistRequestDTO;
import com.alltogether.lvupbackend.smalltalk.checklist.dto.UserChecklistResponseDto;
import com.alltogether.lvupbackend.smalltalk.checklist.entity.ChecklistMaster;
import com.alltogether.lvupbackend.smalltalk.checklist.entity.UserRatingChecklistDetail;
import com.alltogether.lvupbackend.smalltalk.checklist.entity.UserRatingChecklistDetailId;
import com.alltogether.lvupbackend.smalltalk.checklist.repository.ChecklistMasterRepository;
import com.alltogether.lvupbackend.smalltalk.user.dto.MatchingUserResponseDto;
import com.alltogether.lvupbackend.smalltalk.user.dto.MatchingUserWithRateeDTO;
import com.alltogether.lvupbackend.smalltalk.user.entity.MatchingUser;
import com.alltogether.lvupbackend.smalltalk.user.repository.FeedbackRepository;
import com.alltogether.lvupbackend.smalltalk.user.repository.MatchingUserRepository;

import com.alltogether.lvupbackend.user.domain.User;
import com.alltogether.lvupbackend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FeedbackService {

    public final FeedbackRepository feedbackRepository;
    public final ChecklistMasterRepository checklistMasterRepository;
    private final MatchingUserRepository matchingUserRepository;
    private final UserRepository userRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, ChecklistMasterRepository checklistMasterRepository,
                           MatchingUserRepository matchingUserRepository, UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.checklistMasterRepository = checklistMasterRepository;
        this.matchingUserRepository = matchingUserRepository;
        this.userRepository = userRepository;
    }

    public List<ChecklistMasterSimpleDto> getFeedbackOptions() {
        return checklistMasterRepository.findAll()
                .stream()
                .map(ChecklistMasterSimpleDto::from)
                .collect(Collectors.toList());
    }

    // 특정 유저의 모든 체크리스트 조회
    public List<UserRatingChecklistDetail> getUserChecklists(Integer matchingUserId) {
        return feedbackRepository.findByIdMatchingUserId(matchingUserId);
    }

    // 새로운 체크리스트 항목 저장
    @Transactional
    public ResponseEntity<?> saveChecklist(Integer userId, UserChecklistRequestDTO request) {
        try {
            // 체크리스트 저장 요청
            log.info("체크리스트 저장 요청. userId: {}, options: {}, score: {}, content: {}", userId, request.getFeedbackOptions(), request.getRatingScore(), request.getRatingContent());

            // 유효성 검사
            if (request == null || request.getFeedbackOptions() == null || request.getFeedbackOptions().isEmpty()) {
                log.error("체크리스트 옵션이 비어있습니다. userId: {}", userId);
                return ResponseEntity.ok(Map.of("message", "체크리스트 옵션을 선택해주세요."));
            }

            // 현재의 가장 최근 매칭된 매칭 유저 아이디 찾아오기
            final MatchingUser matchingUser = matchingUserRepository.findTopByUserIdOrderByMatchingUserIdDesc(userId);
            if (matchingUser == null) {
                log.error("매칭된 사용자를 찾을수 없습니다. userId: {}", userId);
                return ResponseEntity.ok(Map.of("message", "매칭된 사용자를 찾을 수 없습니다."));
            }

            // 옵션들을 엔티티로 변환
            List<UserRatingChecklistDetail> checklistDetails = request.getFeedbackOptions().stream()
                    .map(option -> {
                        Integer checklistMasterId = option;
                        ChecklistMaster checklistMaster = checklistMasterRepository.findById(checklistMasterId)
                                .orElseThrow(() -> new EntityNotFoundException("체크리스트를 찾을 수 없습니다: " + checklistMasterId));

                        UserRatingChecklistDetailId id = new UserRatingChecklistDetailId(
                                checklistMaster.getChecklistMasterId(),
                                matchingUser.getMatchingUserId());

                        return UserRatingChecklistDetail.builder()
                                .id(id)
                                .checklistMaster(checklistMaster)
                                .matchingUser(matchingUser)
                                .build();
                    })
                    .toList();

            // 평점과 내용 업데이트
            MatchingUser updatedMatchingUser = MatchingUser.builder()
                    .matchingUserId(matchingUser.getMatchingUserId())
                    .userId(matchingUser.getUserId())
                    .rateeId(matchingUser.getRateeId())
                    .matchingId(matchingUser.getMatchingId())
                    .startAt(matchingUser.getStartAt())
                    .userAvatar(matchingUser.getUserAvatar())  // 기존 avatar 유지
                    .accept(matchingUser.getAccept())          // 기존 accept 상태 유지
                    .extend(matchingUser.getExtend())          // 기존 extend 상태 유지
                    .endAt(matchingUser.getEndAt())           // 기존 종료 시간 유지
                    .report(matchingUser.getReport())         // 기존 report 상태 유지
                    .ratingScore(request.getRatingScore())
                    .ratingContent(String.format("{\"content\":\"%s\"}",
                            request.getRatingContent() != null ? request.getRatingContent().replace("\"", "\\\"") : ""))    // JSON 변환
                    .build();

            // 데이터 저장
            matchingUserRepository.save(updatedMatchingUser);
            feedbackRepository.saveAll(checklistDetails);

            log.info("체크리스트 저장 완료. userId: {}", userId);
            return ResponseEntity.ok(Map.of("message", "체크리스트가 성공적으로 저장되었습니다."));

        }  catch (EntityNotFoundException e) {
            log.error("체크리스트 저장 중 엔티티를 찾을 수 없습니다: {}", e.getMessage());
            return ResponseEntity.ok(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("체크리스트 저장 중 오류 발생: ", e);
            return ResponseEntity.ok(Map.of("message", "체크리스트 저장 중 오류가 발생했습니다."));
        }
    }

    // 특정 유저의 특정 매치 체크리스트 상세 조회
    public ResponseEntity<?> getUserChecklistDetail(Integer matchingId, Integer userId) {
        try {
            // 매칭 아이디 존재 확인
//            MatchingUser matchingUser = matchingUserRepository.findById(matchingUserId)
//                    .orElseThrow(()-> new EntityNotFoundException("매칭 정보를 찾을 수 없습니다."));

            // 매치 아이디와 매칭아이디로 매칭유저 찾기
            MatchingUser matchingUser = matchingUserRepository.findByMatchingId_MatchingIdAndUserId(matchingId, userId);
            Integer matchingUserId = matchingUser.getMatchingUserId();

            // 유저 아이디 일치 확인
<<<<<<< HEAD
            if (!matchingUser.getUserId().equals(userId)) {
                log.error("유저 아이디와 매칭 유저 아이디가 일치하지 않습니다.");
                return ResponseEntity.ok(Map.of("message", "권한이 없습니다."));
            }

=======
//            if (!matchingUser.getUserId().equals(userId)) {
//                log.error("유저 아이디와 매칭 유저 아이디가 일치하지 않습니다.");
//                return ResponseEntity.badRequest().body("권한이 없습니다.");
//            }
            log.info("평가자 정보 조회 시작 {} : {}", matchingId, userId);
>>>>>>> develop
            // rateeId 사용자 정보 조회
            User ratee = userRepository.findById(matchingUser.getRateeId())
                    .orElseThrow(() -> new EntityNotFoundException("평가 대상자를 찾을 수 없습니다."));

            // 같은 matchingId를 가진 다른 사용자의 평가 정보 조회
            MatchingUser raterInfo = matchingUserRepository.findByMatchingId_MatchingIdAndUserIdNot(matchingUser.getMatchingId().getMatchingId(), matchingUser.getUserId());
            if (raterInfo == null) {
                log.error("평가자 정보가 존재하지 않습니다.");
                return ResponseEntity.ok(Map.of("message", "평가자 정보가 존재하지 않습니다."));
            }

            // 유저가 고른 체크리스트 조회
            List<String> checklistNames = null;
            List<UserRatingChecklistDetail> details = feedbackRepository.findByIdMatchingUserId(raterInfo.getMatchingUserId());
            if (details.isEmpty()) {
<<<<<<< HEAD
                log.warn("체크리스트 정보가 없습니다. matchingUserId: {}", matchingUserId);
                return ResponseEntity.ok(Map.of("message", "체크리스트 정보가 없습니다."));
=======
                log.warn("체크리스트 정보가 없습니다. matchingUserId: {}", raterInfo.getMatchingUserId());
                // return ResponseEntity.badRequest().body("체크리스트 정보가 없습니다.");
            } else {
                // 체크리스트 이름 조회
                checklistNames = details.stream()
                        .map(detail -> detail.getChecklistMaster().getName())
                        .toList();
>>>>>>> develop
            }



            log.info("매칭: {}, 조회 요청자: {}, 평가자: {}, 점수: {}, 내용: {}, 체크리스트: {}", matchingId, userId, raterInfo.getUserId(), raterInfo.getRatingScore(), raterInfo.getRatingContent(), checklistNames);
            // DTO 변환
            UserChecklistResponseDto response = UserChecklistResponseDto.builder()
                    .checklistNames(checklistNames)
                    .rateeNickname(ratee.getNickname())
                    .ratingContent(raterInfo.getRatingContent())
                    .ratingScore(raterInfo.getRatingScore())
                    .rateeAvatarId(ratee.getAvatar().getAvatarId())
                    .startAt(matchingUser.getStartAt())
                    .build();

            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {
            log.error("체크리스트 상세 조회 중 엔티티를 찾을 수 없습니다: {}", e.getMessage());
            return ResponseEntity.ok(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("체크리스트 상세 조회중 오류 발생: ", e);
            return ResponseEntity.ok(Map.of("message", "조회 중 오류가 발생했습니다."));
        }



//        List<UserRatingChecklistDetail> details = feedbackRepository.findByIdMatchingUserId(matchingId);
//        return details.stream()
//                .map(UserChecklistResponseDto::from)
//                .collect(Collectors.toList());
    }

    // 특정 유저의 모든 피드백 리스트 조회 (페이징, 정렬 적용)
    public ResponseEntity<?> getUserChecklists(Integer userId, Integer page, String orderBy) {
        try {
            // userId를 가진 모든 matchingUserId 조회 + 페이징 + 정렬
            int pageSize = 10;
            int offset = (page - 1) * pageSize;
            //Pageable pageable = PageRequest.of(page - 1, pageSize);

            log.info("피드백 리스트 조회. userId: {}, page: {}, order: {}", userId, page, orderBy);

            // 페이징 설정
            PageRequest pageRequest = PageRequest.of(page - 1, 10);

            // 데이터 조회
            Page<MatchingUserWithRateeDTO> pageResult = matchingUserRepository.findMatchingUsersByUserIdWithPaging(userId, orderBy, pageRequest);

            if (pageResult.isEmpty()) {
                log.warn("조회된 결과가 없습니다.");
                return ResponseEntity.ok(
                        Map.of(
                                "content", Collections.emptyList(),
                                "totalPage", 0,
                                "totalElements", 0,
                                "currentPage", page,
                                "message", "조회된 결과가 없습니다."
                        ));
            }

            Map<String, Object> response = Map.of(
                "content", pageResult.getContent(),
                "totalPage", pageResult.getTotalPages(),
                "totalElements", pageResult.getTotalElements(),
                "currentPage", page,
                "message", "조회 성공"
            );

            return ResponseEntity.ok(response);

//            List<MatchingUserWithRateeDTO> details;
//            if ("recently".equals(orderBy)) {
//                log.info("최신 시작 시간순 정렬");
//                details = matchingUserRepository.findMatchingUsersByUserId(userId, orderBy);
//            } else {
//                // 조건 추가
//                log.info("매칭 아이디순 정렬");
//                details = matchingUserRepository.findMatchingUsersByUserId(userId, "matchingUserId");
//            }
//
//            if (details.isEmpty())  {
//                log.warn("조회된 결과가 없습니다.");
//                return ResponseEntity.ok("조회된 결과가 없습니다.");
//            }

//            return ResponseEntity.ok(details);
        } catch (EntityNotFoundException e) {
            log.error("사용자를 찾을 수 없습니다. userId: {}", userId, e);
            return ResponseEntity.ok(Map.of("message", "사용자를 찾을 수 없습니다."));
        } catch (Exception e) {
            log.error("피드백 리스트 조회중 오류 발생. userId: {}", userId, e);
            return ResponseEntity.ok(Map.of("message", "조회중 오류가 발생하였습니다."));
        }
    }

    /*
     * public List<UserChecklistResponseDto> getUserChecklists(Integer userId,
     * Integer page, String orderBy) {
     * try {
     * // userId로 matchingUserId 조회
     * Integer matchingUserId =
     * matchingUserRepository.findMatchingUserIdByUserId(userId)
     * .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));
     * 
     * // 페이지 크기 설정 (예: 10개씩)
     * int pageSize = 10;
     * int offset = (page - 1) * pageSize;
     * 
     * List<UserRatingChecklistDetail> details;
     * if ("id".equals(orderBy)) {
     * details = feedbackRepository.findAllWithPagingOrderById(matchingUserId,
     * pageSize, offset);
     * } else {
     * // 다른 정렬 기준이 필요한 경우 여기에 추가
     * details = feedbackRepository.findAllWithPagingOrderById(matchingUserId,
     * pageSize, offset);
     * }
     * 
     * return details.stream()
     * .map(UserChecklistResponseDto::from)
     * .collect(Collectors.toList());
     * } catch (EntityNotFoundException e) {
     * log.error("사용자를 찾을 수 없습니다. userId: {}", userId, e);
     * return Collections.emptyList();
     * } catch (Exception e) {
     * log.error("체크리스트 조회 중 오류 발생", e);
     * return Collections.emptyList();
     * }
     * }
     */

//    public ResponseEntity<?> saveChecklist(Integer userId, UserChecklistRequestDTO request) {
//        try {
//            MatchingUser matchingUser = matchingUserRepository.findTopByUserIdOrderByMatchingUserIdDesc(userId);
        // MatchingUser matchingUser = matchingUserRepository.findById(matchingUserId)
        // .orElseThrow(() -> new EntityNotFoundException("매칭된 사용자를 찾을 수 없습니다."));
//
//            log.info("체크리스트 저장 요청 userId: {}, matchingUserId: {}", userId, matchingUser.getUserId());
//            List<UserRatingChecklistDetail> checklistDetails = options.stream()
//                    .map(option -> {
//                        Integer checklistMasterId = Integer.parseInt(option);
//                        ChecklistMaster checklistMaster = checklistMasterRepository.findById(checklistMasterId)
//                                .orElseThrow(() -> new EntityNotFoundException("체크리스트를 찾을 수 없습니다."));
//                        UserRatingChecklistDetailId id = new UserRatingChecklistDetailId(
//                                checklistMaster.getChecklistMasterId(),
//                                matchingUser.getMatchingUserId());
//
//                        return UserRatingChecklistDetail.builder()
//                                .id(id)
//                                .checklistMaster(checklistMaster)
//                                .matchingUser(matchingUser)
//                                .build();
//                    })
//                    .collect(Collectors.toList());
//            log.info("체크리스트 디테일: {}", checklistDetails);
//
//            feedbackRepository.saveAll(checklistDetails);
//            return true;
//        } catch (Exception e) {
//            log.error("체크리스트 저장 중 오류 발생 userId: {}", userId, e);
//            return false;
//        }
//    }

}
