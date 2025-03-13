package com.alltogether.lvupbackend.smalltalk.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MatchDatesResponseDTO {
    private List<String> dates;
    private int total;
}
