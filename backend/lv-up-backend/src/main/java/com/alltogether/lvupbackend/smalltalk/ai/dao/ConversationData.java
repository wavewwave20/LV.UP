package com.alltogether.lvupbackend.smalltalk.ai.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationData {
    @JsonProperty("conversation_history")
    private String conversationHistory;

    @JsonProperty("conversation_records")
    private List<ConversationRecord> conversationRecords;

    @JsonProperty("sentiment_score_sum")
    private int sentimentScoreSum;

    @JsonProperty("end_flag")
    private boolean endFlag;

    @JsonProperty("end_reason")
    private String endReason;
}

