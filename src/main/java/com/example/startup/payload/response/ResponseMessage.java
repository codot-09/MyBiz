package com.example.startup.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResponseMessage (
        String text,
        LocalDateTime sentAt
){
}
