package com.example.startup.payload.response;

import com.example.startup.entity.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResponseUser (
        Long id,
        String fullname,
        String email,
        UserRole role
){
}
