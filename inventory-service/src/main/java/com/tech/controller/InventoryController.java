package com.tech.controller;

import com.tech.model.Inventory;
import com.tech.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {
    @GetMapping
    public List<Inventory> findAll(){
        return InventoryRepository.findAll();
    }
}
