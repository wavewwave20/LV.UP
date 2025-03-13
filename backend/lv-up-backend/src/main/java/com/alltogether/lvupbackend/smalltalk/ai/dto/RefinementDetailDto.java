package com.alltogether.lvupbackend.smalltalk.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefinementDetailDto {

    private int score;

    private String explanation;
}