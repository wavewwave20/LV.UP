package com.alltogether.lvupbackend.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RegisterCompleteDto {
    private List<Integer> interest;
    private Integer avatar_id;
}