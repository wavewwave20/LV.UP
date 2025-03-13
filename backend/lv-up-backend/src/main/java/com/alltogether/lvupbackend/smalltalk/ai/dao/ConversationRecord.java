package com.alltogether.lvupbackend.smalltalk.ai.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationRecord {
    private int turn;
    private String speaker;
    @JsonProperty("speaker_type")
    private String speakerType;
    private String text;
    @JsonProperty("sentiment_score")
    private int sentimentScore;
}
