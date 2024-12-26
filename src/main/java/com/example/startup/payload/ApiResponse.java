package com.example.startup.payload;

import com.example.startup.payload.response.ResponseTransaction;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private Object data;
    private List<ResponseTransaction> transactions;

    public ApiResponse(Object data,List<ResponseTransaction> transactions) {
        this.data = data;
        this.transactions = transactions;
    }

}
