package com.alltogether.lvupbackend.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserInfoDto {

    private String nickname;
    private String introduction;
    private Integer birthyear;
    private List<String> interests;
}

