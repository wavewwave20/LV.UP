package com.alltogether.lvupbackend.login.dto;

import lombok.Data;

@Data
public class RegisterResponseDto {
    private String userNanoId;
    private boolean registerComplete;
}
