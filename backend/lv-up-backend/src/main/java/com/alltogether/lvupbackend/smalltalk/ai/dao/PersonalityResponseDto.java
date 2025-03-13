package com.alltogether.lvupbackend.smalltalk.ai.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PersonalityResponseDto {
    private int id;
    private String name;
    private String level;
    private String emoji;
}
