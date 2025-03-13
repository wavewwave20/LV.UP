package com.alltogether.lvupbackend.smalltalk.checklist.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserChecklistRequestDTO {

    @JsonProperty("feedbackOptions")
    private List<Integer> feedbackOptions;

    @JsonProperty("ratingScore")
    private Byte ratingScore;

    @JsonProperty("ratingContent")
    private String ratingContent;
}
