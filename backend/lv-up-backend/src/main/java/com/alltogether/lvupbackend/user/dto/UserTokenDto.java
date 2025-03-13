package com.alltogether.lvupbackend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTokenDto {
    private String token;
    private String tokenType = "Bearer";
}