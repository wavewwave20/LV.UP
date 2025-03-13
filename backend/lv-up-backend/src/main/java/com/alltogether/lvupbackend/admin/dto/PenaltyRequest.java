package com.alltogether.lvupbackend.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class PenaltyRequest {

    @JsonProperty("penaltyOptions")
    private List<Integer> penaltyOptions;

    @JsonProperty("endAt")
    private LocalDateTime endAt;
}
