package com.tech.controller;

import com.tech.model.Order;
import com.tech.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    public Order createOrders(@RequestParam(name = "productId") String productId,
                              @RequestParam(name = "quantity") int quantity,
                              @RequestParam(name = "userId") String userId
    ){
        log.info("Creating order for product {} with quantity {} from {}",
                    productId,quantity,userId
                );

        return orderService.createOrder(productId,quantity,userId);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(){
        return ResponseEntity.ok(orderService.findAllOrders());
    }


}
