package com.alltogether.lvupbackend.smalltalk.user.repository;

import com.alltogether.lvupbackend.smalltalk.user.dto.MatchingUserWithRateeDTO;
import com.alltogether.lvupbackend.smalltalk.user.entity.Matching;
import com.alltogether.lvupbackend.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.alltogether.lvupbackend.smalltalk.user.entity.MatchingUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

public interface MatchingUserRepository extends JpaRepository<MatchingUser, Integer> {

    // userId로 matchingUserId 조회
    //@Query("SELECT m.matchingUserId FROM MatchingUser m WHERE m.userId = :userId")
    Optional<Integer> findMatchingUserIdByUserId(@Param("userId") Integer userId);

    // matchingId로  userId들 조회
    @Query("SELECT mu.userId FROM MatchingUser mu WHERE mu.matchingId.matchingId = :matchingId")
    List<Integer> findUserIdsByMatchingId(@Param("matchingId") Integer matchingId);

    // userId로 MatchingUser 조회, 최신순 정렬
    // @Query("SELECT m FROM MatchingUser m WHERE m.userId = :userId ORDER BY m.matchingUserId DESC LIMIT 1")
    // MatchingUser findMatchingUserIdByUserIdDESC(@Param("userId") Integer userId);
    MatchingUser findTopByUserIdOrderByMatchingUserIdDesc(Integer userId);

    // 평가자 조회
    MatchingUser findByMatchingId_MatchingIdAndUserIdNot(Integer matchingId, Integer userId);

    // userId로 MatchingUser 조회, 최신순 정렬
    @Query(value = "SELECT new com.alltogether.lvupbackend.smalltalk.user.dto.MatchingUserWithRateeDTO(" +
            "m.matchingUserId, m.ratingScore, m.startAt, " +
            "(SELECT u.nickname FROM User u WHERE u.userId = m.rateeId))" +
            "FROM MatchingUser m " +
            "WHERE m.userId = :userId " +
            "ORDER BY " +
            "CASE " +
            "    WHEN :orderBy = 'matchingUserId' THEN CAST(m.matchingUserId as string) " +
            "    WHEN :orderBy = 'recently' THEN CAST(m.startAt as string) " +
            "    WHEN :orderBy = 'endAt' THEN CAST(m.endAt as string) " +
            "    ELSE CAST(m.matchingUserId as string) " +
            "END DESC")
    List<MatchingUserWithRateeDTO> findMatchingUsersByUserId(
            @Param("userId") Integer userId,
            @Param("orderBy") String orderBy);

    @Query("SELECT m FROM MatchingUser m WHERE m.userId = :userId ORDER BY :orderby ASC LIMIT :pageSize OFFSET :offset")
    List<MatchingUser> findMatchingUserIdsByUserIdASC(
            @Param("userId") Integer userId,
            @Param("pageSize") Integer pageSize,
            @Param("offset") Integer offset,
            @Param("orderby") String orderby);

<<<<<<< HEAD
    // userId로 MatchingUser 조회, 최신순 정렬 페이징 추가
    @Query(value = "SELECT new com.alltogether.lvupbackend.smalltalk.user.dto.MatchingUserWithRateeDTO(" +
            "m.matchingUserId, m.ratingScore, m.startAt, " +
            "(SELECT u.nickname FROM User u WHERE u.userId = m.rateeId))" +
            "FROM MatchingUser m " +
            "WHERE m.userId = :userId " +
            "ORDER BY " +
            "CASE " +
            "    WHEN :orderBy = 'matchingUserId' THEN CAST(m.matchingUserId as string) " +
            "    WHEN :orderBy = 'recently' THEN CAST(m.startAt as string) " +
            "    WHEN :orderBy = 'endAt' THEN CAST(m.endAt as string) " +
            "    ELSE CAST(m.matchingUserId as string) " +
            "END DESC")
    Page<MatchingUserWithRateeDTO> findMatchingUsersByUserIdWithPaging(
            @Param("userId") Integer userId,
            @Param("orderBy") String orderBy,
            Pageable pageable);
=======
        // userId로 MatchingUser 조회, 최신순 정렬
        @Query(value = "SELECT new com.alltogether.lvupbackend.smalltalk.user.dto.MatchingUserWithRateeDTO(" +
                        "m.matchingUserId, m.ratingScore, m.startAt, " +
                        "(SELECT u.nickname FROM User u WHERE u.userId = m.rateeId))" +
                        "FROM MatchingUser m " +
                        "WHERE m.userId = :userId " +
                        "ORDER BY " +
                        "CASE " +
                        "    WHEN :orderBy = 'matchingUserId' THEN CAST(m.matchingUserId as string) " +
                        "    WHEN :orderBy = 'recently' THEN CAST(m.startAt as string) " +
                        "    WHEN :orderBy = 'endAt' THEN CAST(m.endAt as string) " +
                        "    ELSE CAST(m.matchingUserId as string) " +
                        "END DESC")
        List<MatchingUserWithRateeDTO> findMatchingUsersByUserId(
                        @Param("userId") Integer userId,
                        @Param("orderBy") String orderBy);

        @Query("SELECT m FROM MatchingUser m WHERE m.userId = :userId ORDER BY :orderby ASC LIMIT :pageSize OFFSET :offset")
        List<MatchingUser> findMatchingUserIdsByUserIdASC(
                        @Param("userId") Integer userId,
                        @Param("pageSize") Integer pageSize,
                        @Param("offset") Integer offset,
                        @Param("orderby") String orderby);

        // userId로 MatchingUser 조회, 최신순 정렬 페이징 추가
        @Query(value = "SELECT new com.alltogether.lvupbackend.smalltalk.user.dto.MatchingUserWithRateeDTO(" +
                        "m.matchingUserId, m.ratingScore, m.startAt, " +
                        "(SELECT u.nickname FROM User u WHERE u.userId = m.rateeId))" +
                        "FROM MatchingUser m " +
                        "WHERE m.userId = :userId " +
                        "ORDER BY " +
                        "CASE " +
                        "    WHEN :orderBy = 'matchingUserId' THEN CAST(m.matchingUserId as string) " +
                        "    WHEN :orderBy = 'recently' THEN CAST(m.startAt as string) " +
                        "    WHEN :orderBy = 'endAt' THEN CAST(m.endAt as string) " +
                        "    ELSE CAST(m.matchingUserId as string) " +
                        "END DESC")
        Page<MatchingUserWithRateeDTO> findMatchingUsersByUserIdWithPaging(
                        @Param("userId") Integer userId,
                        @Param("orderBy") String orderBy,
                        Pageable pageable);

        @Query("SELECT DISTINCT FUNCTION('DATE_FORMAT', mu.startAt, '%Y%m%d') FROM MatchingUser mu " +
                        "WHERE mu.userId = :userId " +
                        "AND mu.startAt BETWEEN :startDate AND :endDate")
        List<String> findDistinctMatchingDatesByUserIdAndPeriod(
                        @Param("userId") Integer userId,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        Optional<MatchingUser> findByMatchingIdAndUserId(Matching matching, int userId);
>>>>>>> develop
}
