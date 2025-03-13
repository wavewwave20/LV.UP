package com.alltogether.lvupbackend.smalltalk.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SessionInfoDTO {
    private String sessionId;
    private String matchingId;
    private List<Integer> userList;
}
