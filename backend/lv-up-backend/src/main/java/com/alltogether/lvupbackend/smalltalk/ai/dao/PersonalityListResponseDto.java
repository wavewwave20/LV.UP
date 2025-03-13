package com.alltogether.lvupbackend.smalltalk.ai.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PersonalityListResponseDto {
    List<PersonalityResponseDto> personalities;
}
