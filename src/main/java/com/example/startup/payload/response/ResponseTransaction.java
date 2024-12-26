package com.example.startup.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResponseTransaction (
        Double amount,
        String description,
        LocalDate date
){
}
