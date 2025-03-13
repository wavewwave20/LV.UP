package com.alltogether.lvupbackend.smalltalk.ai.service;

import com.alltogether.lvupbackend.smalltalk.ai.dto.ScoreResponseDto;
import com.alltogether.lvupbackend.smalltalk.ai.entity.AiSmalltalkScore;
import com.alltogether.lvupbackend.smalltalk.ai.repository.AiSmalltalkScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AiSmalltalkScoreService {

    @Autowired
    private AiSmalltalkScoreRepository repository;

    public ScoreResponseDto getMonthlyScores(int year, int month, int userId) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        int days = startDate.lengthOfMonth();

        // 해당 월의 모든 점수 데이터를 조회 (연도, 월, userId 조건)
        List<AiSmalltalkScore> scores = repository.findByYearMonthUserId(year, month, userId);

        // 날짜별 응답 데이터 초기화
        List<List<Integer>> dailyDetail = new ArrayList<>(days);
        List<Integer> dailyOverall = new ArrayList<>(days);

        // 1일부터 days일까지 미리 기본값(빈 리스트, 0)을 할당
        for (int day = 0; day < days; day++) {
            // 각 날짜의 상세 점수(5개)를 0으로 초기화
            dailyDetail.add(new ArrayList<>(Collections.nCopies(5, 0)));
            dailyOverall.add(0);
        }

        // 전체 점수 건수를 저장할 배열 (dailyOverall 평균 계산용)
        int[] overallCount = new int[days];
        // 각 날짜의 scoreId별 건수를 저장 (평균 계산용)
        int[][] detailCount = new int[days][5];

        // 각 점수를 해당 날짜의 리스트에 추가 (날짜는 createdAt의 dayOfMonth 사용)
        for (AiSmalltalkScore score : scores) {
            int dayOfMonth = score.getCreatedAt().getDayOfMonth();
            int index = dayOfMonth - 1;  // 리스트 인덱스는 0부터 시작

            int scoreId = score.getScoreId().intValue();
            // scoreId가 1~5 사이일 것으로 가정
            if (scoreId >= 1 && scoreId <= 5) {
                int currentValue = dailyDetail.get(index).get(scoreId - 1);
                dailyDetail.get(index).set(scoreId - 1, currentValue + score.getScore().intValue());
                detailCount[index][scoreId - 1]++; // 해당 카테고리의 건수 증가
            }

            // dailyOverall에 해당 날짜의 점수를 합산
            dailyOverall.set(index, dailyOverall.get(index) + score.getScore().intValue());
            overallCount[index]++;
        }

        // 각 날짜별 각 카테고리의 평균 및 dailyOverall 평균 계산
        for (int day = 0; day < days; day++) {
            for (int k = 0; k < 5; k++) {
                if (detailCount[day][k] != 0) {
                    int averageScore = dailyDetail.get(day).get(k) / detailCount[day][k];
                    dailyDetail.get(day).set(k, averageScore);
                }
            }
            if (overallCount[day] != 0) {
                int averageOverall = dailyOverall.get(day) / overallCount[day];
                dailyOverall.set(day, averageOverall);
            }
        }

        // DTO 생성 후 값 세팅
        ScoreResponseDto response = new ScoreResponseDto();
        response.setMonth(month);
        response.setDays(days);
        response.setDailyDetail(dailyDetail);
        response.setDailyOverall(dailyOverall);
        return response;
    }
}
