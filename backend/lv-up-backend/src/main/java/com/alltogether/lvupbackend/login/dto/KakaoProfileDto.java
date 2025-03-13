package com.alltogether.lvupbackend.login.dto;

import lombok.Data;

@Data
public class KakaoProfileDto {
    private String id;
    private String gender;
    private int birthyear;
}
