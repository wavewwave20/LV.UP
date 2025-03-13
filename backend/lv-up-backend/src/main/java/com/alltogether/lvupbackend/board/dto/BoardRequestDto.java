package com.alltogether.lvupbackend.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardRequestDto {
    private Integer userId;
    private String type;
    private String boardTitle;
    private String boardContent;
    private Integer visionable;
}
