package com.alltogether.lvupbackend.smalltalk.ai.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefinementExplanationResponseDto {

    private int turn;

    // JSON의 "scores" 필드를 Map으로 매핑 (키는 String, 값은 ScoreDetailDto)
    private Map<String, RefinementDetailDto> scores;

    // JSON 필드명이 snake_case인 경우 @JsonProperty를 이용해 매핑
    @JsonProperty("overall_score")
    private int overallScore;

    @JsonProperty("overall_comment")
    private String overallComment;

    @JsonProperty("end_flag")
    private boolean endFlag;

}
