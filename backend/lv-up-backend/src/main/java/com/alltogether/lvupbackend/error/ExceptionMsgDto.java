package com.alltogether.lvupbackend.error;

import lombok.Data;

@Data
public class ExceptionMsgDto {
    private String message;

    public ExceptionMsgDto(String message) {
        this.message = message;
    }
}
