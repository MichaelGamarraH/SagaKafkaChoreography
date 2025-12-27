package com.tech.repository;

import com.tech.model.AccountDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class BankBalanceRepository {

    private static final Map<String,Long> ACCOUNTS_DB = new HashMap<>();
    private static long MINIMUM_BALANCE_REQUIRED = 100;

    public static boolean verifyBalanceAndPay(String userId, long paymentAmount) {
        long availableBalance = ACCOUNTS_DB.get(userId);
        if ((availableBalance - MINIMUM_BALANCE_REQUIRED) < paymentAmount) {
            return Boolean.FALSE;
        } else {
            ACCOUNTS_DB.put(userId, (availableBalance - paymentAmount));
            return Boolean.TRUE;
        }
    }

    static{
        dataAccounts();
    }

    public static List<AccountDTO> findAll(){
        return ACCOUNTS_DB.entrySet().stream()
                .map(a-> new AccountDTO(a.getKey(),a.getValue()))
                .toList();
    }

    private static void dataAccounts() {
        ACCOUNTS_DB.put("101", 3000L);
        ACCOUNTS_DB.put("102", 6000L);
        ACCOUNTS_DB.put("103", 9000L);
    }

}
