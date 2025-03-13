package com.alltogether.lvupbackend.smalltalk.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartConversationResponseDto {

    @JsonProperty("ai_conversation_id")
    private int aiConversationId;

    @JsonProperty("scenario_name")
    private String scenarioName;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    private String initOpSpeaking;
}
