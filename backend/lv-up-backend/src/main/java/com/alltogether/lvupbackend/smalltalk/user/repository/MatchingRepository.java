package com.alltogether.lvupbackend.smalltalk.user.repository;

import com.alltogether.lvupbackend.smalltalk.user.dto.MatchHistoryResponseDto;
import com.alltogether.lvupbackend.smalltalk.user.entity.Matching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface MatchingRepository extends JpaRepository<Matching, Integer> {
    @Query(value = """
    SELECT new com.alltogether.lvupbackend.smalltalk.user.dto.MatchHistoryResponseDto(
        m.matchingId,
        u2.nickname,
        mu1.startAt,
        mu2.ratingScore,
        mm.modeContent,
        CAST(COALESCE(mu1.report, 0) AS byte)
    )
    FROM MatchingUser mu1
    JOIN MatchingUser mu2 ON mu1.matchingId = mu2.matchingId AND mu1.userId != mu2.userId
    JOIN User u2 ON mu2.userId = u2.userId
    JOIN Matching m ON mu1.matchingId = m
    JOIN MatchMode mm ON m.matchingModeId = mm.matchingModeId
    WHERE mu1.userId = :userId
    AND DATE(mu1.startAt) = DATE(:searchDate)
    ORDER BY mu1.startAt DESC
    """,
    countQuery = """
    SELECT COUNT(m.matchingId)
    FROM MatchingUser mu1
    JOIN MatchingUser mu2 ON mu1.matchingId = mu2.matchingId AND mu1.userId != mu2.userId
    JOIN User u2 ON mu2.userId = u2.userId
    JOIN Matching m ON mu1.matchingId = m
    JOIN MatchMode mm ON m.matchingModeId = mm.matchingModeId
    WHERE mu1.userId = :userId
    AND DATE(mu1.startAt) = DATE(:searchDate)
    """)
    Page<MatchHistoryResponseDto> findMatchHistoryByUserIdAndDate(
            Integer userId,
            LocalDateTime searchDate,
            Pageable pageable
    );
    // 매칭 아이디가 동일한 유저 찾아서 조인하고
    // 매칭된 상대의 이름을 가져오기위한 조인
    // 매칭을 조회하기위한 조인
    // 매칭 모드 이름을 위한 조인

}
