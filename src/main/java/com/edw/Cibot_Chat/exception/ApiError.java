package com.edw.Cibot_Chat.exception;

import java.time.Instant;

import lombok.Getter;

@Getter
public class ApiError {

    private String code;
    private String message;
    private Instant timestamp;
    private String path;

    public ApiError(String code, String message, Instant timestamp, String path){
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
        this.path = path;
    }
    
}
