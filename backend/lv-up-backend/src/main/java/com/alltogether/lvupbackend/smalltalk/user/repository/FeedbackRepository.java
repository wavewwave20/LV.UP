package com.alltogether.lvupbackend.smalltalk.user.repository;

import com.alltogether.lvupbackend.smalltalk.checklist.entity.UserRatingChecklistDetail;
import com.alltogether.lvupbackend.smalltalk.checklist.entity.UserRatingChecklistDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<UserRatingChecklistDetail, UserRatingChecklistDetailId> {
    
    // matchingUserId로 해당 유저의 모든 체크리스트 조회
    //@Query("SELECT u FROM UserRatingChecklistDetail u WHERE u.id.matchingUserId = :matchingUserId")
    //List<UserRatingChecklistDetail> findAllByMatchingUserId(@Param("matchingUserId") Integer matchingUserId);
    List<UserRatingChecklistDetail> findByIdMatchingUserId(Integer matchingUserId);
    
    // matchingUserId와 checklistMasterId로 특정 체크리스트 조회
    @Query("SELECT u FROM UserRatingChecklistDetail u WHERE u.id.matchingUserId = :matchingUserId AND u.id.checklistMasterId = :checklistMasterId")
    UserRatingChecklistDetail findByMatchingUserIdAndChecklistMasterId(
        @Param("matchingUserId") Integer matchingUserId,
        @Param("checklistMasterId") Integer checklistMasterId
    );
    
    // 특정 matchingUserId의 체크리스트 존재 여부 확인
    boolean existsByIdMatchingUserId(Integer matchingUserId);

    // 페이징과 정렬을 적용한 체크리스트 조회
    @Query("SELECT u FROM UserRatingChecklistDetail u " +
            "WHERE u.id.matchingUserId = :matchingUserId " +
            "ORDER BY u.id.matchingUserId DESC " +
            "LIMIT :pageSize OFFSET :offset")
    List<UserRatingChecklistDetail> findAllWithPagingOrderById(
            @Param("matchingUserId") Integer matchingUserId,
            @Param("pageSize") Integer pageSize,
            @Param("offset") Integer offset
    );
}