package com.alltogether.lvupbackend.admin.dto;

import lombok.Data;

@Data
public class AdminDto {
    private String id;
    private String nickname;
    private String name;
    private String introduction;
    private String nanoid;
    private int userImageKey;
}

