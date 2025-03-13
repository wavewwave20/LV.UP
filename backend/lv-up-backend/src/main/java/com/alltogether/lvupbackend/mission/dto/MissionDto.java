package com.alltogether.lvupbackend.mission.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MissionDto {
    private Integer missionId;
    private String name;
    private String description;
    private Integer goal;
    private Character category;
    private Integer coin;
    private Integer ticket;
    private Integer exp;
    @JsonProperty("is_completed")
    private boolean isCompleted;
    @JsonProperty("reward_claimed")
    private boolean rewardClaimed;
}