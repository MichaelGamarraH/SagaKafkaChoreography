package com.tech.repository;

import com.tech.model.Transaction;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class TransactionRepository {
    private static final Map<String, Transaction> DB = new HashMap<>();

    public static void save(Transaction transaction) {
        log.info("Saving transaction {}", transaction);
        DB.put(transaction.transactionId(), transaction);
        log.info("Saved transaction {}", transaction);
    }

    public static Transaction get(String transactionId) {
        return DB.get(transactionId);
    }

    public static List<Transaction> getAll() {
        return DB.values().stream().toList();
    }
}
