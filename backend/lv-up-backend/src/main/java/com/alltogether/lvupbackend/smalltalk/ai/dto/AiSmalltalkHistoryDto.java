package com.alltogether.lvupbackend.smalltalk.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AiSmalltalkHistoryDto {
    private int aiConversationId;
    private int overallScore;
    private boolean isProcessed;
    private LocalDateTime createdAt;
    private String opName;
    private int opAge;
    private String opGender;
    private String opPersonality;
    private String description;
}
