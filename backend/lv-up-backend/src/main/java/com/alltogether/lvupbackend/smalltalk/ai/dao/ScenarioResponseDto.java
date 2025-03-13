package com.alltogether.lvupbackend.smalltalk.ai.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ScenarioResponseDto {

    private int id;

    @JsonProperty("scenario_name")
    private String senarioName;

    @JsonProperty("m_name")
    private String mName;

    @JsonProperty("f_name")
    private String fNmae;

    private String emoji;

    private String description;

    private String initOpSpeaking;
}
