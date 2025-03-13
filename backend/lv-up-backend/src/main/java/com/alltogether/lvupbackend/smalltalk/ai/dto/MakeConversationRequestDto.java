package com.alltogether.lvupbackend.smalltalk.ai.dto;

import com.alltogether.lvupbackend.smalltalk.ai.dao.ConversationData;
import com.alltogether.lvupbackend.smalltalk.ai.dao.ConversationRecord;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MakeConversationRequestDto {

    @JsonProperty("ai_smalltalk_id")
    private int aiSmalltalkId;

    @JsonProperty("conversation_history")
    private String conversationHistory;

    @JsonProperty("conversation_records")
    private List<ConversationRecord> conversationRecords;

    @JsonProperty("sentiment_score_sum")
    private int sentimentScoreSum;

    @JsonProperty("u_name")
    private String uName;

    @JsonProperty("op_name")
    private String opName;

    @JsonProperty("user_input")
    private String userInput;

    @JsonProperty("this_turn")
    private int thisTurn;

    public MakeConversationRequestDto(int aiSmalltalkId,ConversationData conversationData, String uName, String opName, String userInput, int thisTurn) {
        this.aiSmalltalkId = aiSmalltalkId;
        this.conversationHistory = conversationData.getConversationHistory();
        this.conversationRecords = conversationData.getConversationRecords();
        this.sentimentScoreSum = conversationData.getSentimentScoreSum();
        this.uName = uName;
        this.opName = opName;
        this.userInput = userInput;
        this.thisTurn = thisTurn;
    }
}

