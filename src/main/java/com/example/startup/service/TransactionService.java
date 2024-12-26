package com.example.startup.service;

import com.example.startup.entity.Transaction;
import com.example.startup.entity.User;
import com.example.startup.entity.enums.TransactionType;
import com.example.startup.payload.ApiResponse;
import com.example.startup.payload.request.RequestTransaction;
import com.example.startup.payload.response.ResponseTransaction;
import com.example.startup.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public ApiResponse addTransaction(User user, TransactionType type, RequestTransaction requestTransaction) {
        Transaction transaction = Transaction.builder()
                .user(user)
                .type(type)
                .amount(requestTransaction.amount())
                .description(requestTransaction.description())
                .build();
        transactionRepository.save(transaction);
        return new ApiResponse("Qo'shildi",null);
    }

    public ApiResponse sumTransaction(User user, TransactionType transactionType) {
        List<Transaction> transactions = transactionRepository.findByUserIdAndTransactionType(user.getId(), transactionType);
        if (transactions.isEmpty()) {
            return new ApiResponse("Tranzaksiyalar topilmadi",null);
        }

        Double sum = transactionRepository.getTransactionSumByUserIdAndType(user.getId(), transactionType);
        List<ResponseTransaction> responseTransactions = transactions.stream()
                .map(this::mapper)
                .collect(Collectors.toList());

        return new ApiResponse("Umumiy summa: " + sum, responseTransactions);
    }

    public ApiResponse checkProfitOrLoss(Long userId) {
        Double incomeSum = transactionRepository.getTransactionSumByUserIdAndType(userId, TransactionType.INCOME);
        Double expenseSum = transactionRepository.getTransactionSumByUserIdAndType(userId, TransactionType.EXPENSE);
        Double difference = incomeSum - expenseSum;

        String status = (incomeSum > expenseSum) ? "Foydada" :
                (incomeSum < expenseSum) ? "Zararda" : "Balansda";

        return new ApiResponse(status + ". Farq: " + difference,null);
    }

    public ApiResponse getTransactionsByDateRange(User user, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = transactionRepository.findTransactionsByUserIdAndDateRange(user.getId(), startDate, endDate);
        if (transactions.isEmpty()) {
            return new ApiResponse("Ushbu vaqt oralig'ida tranzaksiyalar topilmadi", null);
        }
        Double totalAmount = transactionRepository.getTransactionSumByUserIdAndDateRange(user.getId(), startDate, endDate);
        List<ResponseTransaction> responseTransactions = transactions.stream()
                .map(this::mapper)
                .collect(Collectors.toList());
        return new ApiResponse("Umumiy summa: " + totalAmount, responseTransactions);
    }

    private ResponseTransaction mapper(Transaction transaction) {
        return new ResponseTransaction(
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getTransactionDate()
        );
    }
}
