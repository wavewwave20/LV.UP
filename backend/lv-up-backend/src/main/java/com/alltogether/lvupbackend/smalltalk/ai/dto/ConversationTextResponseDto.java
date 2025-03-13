package com.alltogether.lvupbackend.smalltalk.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationTextResponseDto {

    @JsonProperty("opponent_answer")
    private String opponentAnswer;

    @JsonProperty("next_suggested_answer")
    private String nextSuggestedAnswer;

    @JsonProperty("end_flag")
    private boolean endFlag;

    @JsonProperty("end_reason")
    private String endReason;

    @JsonProperty("sentiment_score_sum")
    private int sentimentScoreSum;
}
