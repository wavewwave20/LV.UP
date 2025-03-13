package com.alltogether.lvupbackend.smalltalk.ai.dto;

import com.alltogether.lvupbackend.smalltalk.ai.dao.ConversationData;
import com.alltogether.lvupbackend.smalltalk.ai.dao.ConversationRecord;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MakeConversationResponseDto {
    @JsonProperty("ai_smalltalk_id")
    private int aiSmalltalkId;

    @JsonProperty("new_conversation_history")
    private String newConversationHistory;

    @JsonProperty("new_conversation_records")
    private List<ConversationRecord> newConversationRecords;

    @JsonProperty("sentiment_score_sum")
    private int sentimentScoreSum;

    @JsonProperty("u_name")
    private String uName;

    @JsonProperty("op_name")
    private String opName;

    @JsonProperty("op_answer")
    private String opAnswer;

    @JsonProperty("next_suggested_answer")
    private String nextSuggestedAnswer;

    @JsonProperty("end_flag")
    private boolean endFlag;

    @JsonProperty("end_reason")
    private String endReason;

    public ConversationData toConversationData() {
        ConversationData conversationData = new ConversationData();
        conversationData.setConversationHistory(newConversationHistory);
        conversationData.setConversationRecords(newConversationRecords);
        conversationData.setSentimentScoreSum(sentimentScoreSum);
        conversationData.setEndFlag(endFlag);
        conversationData.setEndReason(endReason);
        return conversationData;
    }
}
