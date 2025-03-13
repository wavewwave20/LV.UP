package com.alltogether.lvupbackend.smalltalk.ai.dto;

import com.alltogether.lvupbackend.smalltalk.ai.dao.ConversationRecord;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AiSmalltalkDialogue {

    @JsonProperty("conversation_history")
    private String conversationHistory;

    @JsonProperty("conversation_records")
    private List<ConversationRecord> conversationRecords;

    @JsonProperty("sentiment_score_sum")
    private int sentimentScoreSum;

    @JsonProperty("end_reason")
    private String endReason;

    private List<RefinementExplanationResponseDto> refinements;
}
