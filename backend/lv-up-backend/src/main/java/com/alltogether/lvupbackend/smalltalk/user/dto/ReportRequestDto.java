package com.alltogether.lvupbackend.smalltalk.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class ReportRequestDto {

    @JsonProperty("matchingId")
    private Integer matchingId;

    @JsonProperty("userId")
    private Integer userId;

    @JsonProperty("reportTypeIds")
    private List<Integer> reportTypeIds;

    @JsonProperty("description")
    private String description;

    public ReportRequestDto(Integer matchingId, Integer userId, List<Integer> reportTypeIds, String description) {
        this.matchingId = matchingId;
        this.userId = userId;
        this.reportTypeIds = reportTypeIds;
        this.description = description;
    }

    public ReportRequestDto(Integer userId, List<Integer> reportTypeIds, String description) {
        this.userId = userId;
        this.reportTypeIds = reportTypeIds;
        this.description = description;
    }
}
