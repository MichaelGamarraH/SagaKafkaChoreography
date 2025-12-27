package com.tech.controller;

import com.tech.model.AccountDTO;
import com.tech.repository.BankBalanceRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/balance")
public class BankBalanceController {


    @GetMapping("/accounts")
    public List<AccountDTO> getAllAccount(){
        return BankBalanceRepository.findAll();
    }

}
