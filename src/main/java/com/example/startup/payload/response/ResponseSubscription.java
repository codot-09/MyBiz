package com.example.startup.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResponseSubscription (
        Long id,
        String fullname,
        String email
){
}
