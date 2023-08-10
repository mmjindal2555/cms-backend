package com.intuit.cms.controllerAdvice;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter @Setter @NoArgsConstructor
public class ErrorResponse{
    private String message;
    private HttpStatus status;

    public ErrorResponse(String message, HttpStatus status){
        this.message = message;
        this.status = status;
    }
}
