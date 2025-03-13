package com.alltogether.lvupbackend.smalltalk.ai.repository;

import com.alltogether.lvupbackend.smalltalk.ai.entity.AiSmalltalkScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiSmalltalkScoreRepository extends JpaRepository<AiSmalltalkScore, Integer> {

    @Query("SELECT s FROM AiSmalltalkScore s JOIN s.aiSmalltalk a " +
            "WHERE FUNCTION('YEAR', a.createdAt) = :year " +
            "AND FUNCTION('MONTH', a.createdAt) = :month " +
            "AND a.user.userId = :userId")
    List<AiSmalltalkScore> findByYearMonthUserId(@Param("year") int year,
                                                 @Param("month") int month,
                                                 @Param("userId") int userId);
}
