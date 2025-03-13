package com.alltogether.lvupbackend.smalltalk.ai.repository;

import com.alltogether.lvupbackend.smalltalk.ai.dto.AiSmalltalkHistoryDto;
import com.alltogether.lvupbackend.smalltalk.ai.entity.AiPersonality;
import com.alltogether.lvupbackend.smalltalk.ai.entity.AiScenario;
import com.alltogether.lvupbackend.smalltalk.ai.entity.AiSmalltalk;
import com.alltogether.lvupbackend.user.domain.User;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AiSmalltalkRepository extends JpaRepository<AiSmalltalk, Integer> {

        Optional<AiSmalltalk> findFirstByUserAndAiPersonalityAndAiScenarioOrderByCreatedAtDesc(@NotNull User user,
                        @NotNull AiPersonality aiPersonality, @NotNull AiScenario aiScenario);

        List<AiSmalltalk> findAllByUserAndEndFlagIsFalse(User user);

        Optional<AiSmalltalk> findFirstByUserAndEndFlagIsFalseOrderByCreatedAtDesc(User user);

        Page<AiSmalltalk> findAllByUserAndCreatedAtBetween(
                        User user,
                        LocalDateTime startOfDay,
                        LocalDateTime endOfDay,
                        Pageable pageable);

        List<AiSmalltalk> findAllByUserAndEndFlagFalse(User user);

        @Query("SELECT DISTINCT FUNCTION('DATE_FORMAT', ai.createdAt, '%Y%m%d') FROM AiSmalltalk ai " +
                        "WHERE ai.user.userId = :userId " +
                        "AND ai.createdAt BETWEEN :startDate AND :endDate")
        List<String> findDistinctAIDatesByUserIdAndPeriod(
                        @Param("userId") Integer userId,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);
}
