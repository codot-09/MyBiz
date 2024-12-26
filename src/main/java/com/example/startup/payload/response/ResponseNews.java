package com.example.startup.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResponseNews (
        Long id,
        String title,
        String context,
        Long userId
){
}
