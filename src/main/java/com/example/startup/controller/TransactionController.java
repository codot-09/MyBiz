package com.example.startup.controller;

import com.example.startup.entity.User;
import com.example.startup.entity.enums.TransactionType;
import com.example.startup.payload.ApiResponse;
import com.example.startup.payload.request.RequestTransaction;
import com.example.startup.security.CurrentUser;
import com.example.startup.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/add")
    @Operation(summary = "Tranzaksiya qo'shish")
    public ResponseEntity<ApiResponse> addTransaction(
            @CurrentUser User user,
            @RequestParam TransactionType type,
            @RequestBody RequestTransaction requestTransaction
            ){
        return ResponseEntity.ok(transactionService.addTransaction(user, type, requestTransaction));
    }

    @GetMapping("/checkBalance")
    @Operation(summary = "Balansni tekshirish")
    public ResponseEntity<ApiResponse> sum(
            @CurrentUser User user,
            @RequestParam TransactionType type
    ){
        return ResponseEntity.ok(transactionService.sumTransaction(user, type));
    }

    @GetMapping("/checkProfit")
    @Operation(summary = "Foyda yoki zararni tekshirish")
    public ResponseEntity<ApiResponse> heckProfitOrLoss(
            @CurrentUser User user
    ){
        return ResponseEntity.ok(transactionService.checkProfitOrLoss(user.getId()));
    }

    @GetMapping("/getTransactionByDate")
    @Operation(summary = "Vaqt oralig'idagi tranzaksiyalarni ko'rish")
    public ResponseEntity<ApiResponse> get(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @CurrentUser User user
            ){
        return ResponseEntity.ok(transactionService.getTransactionsByDateRange(user, startDate, endDate));
    }
}
