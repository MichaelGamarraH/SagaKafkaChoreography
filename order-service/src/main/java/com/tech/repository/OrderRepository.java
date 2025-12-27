package com.tech.repository;

import com.tech.model.Order;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class OrderRepository {

    private static final Map<String, Order> DB = new HashMap<>();

    public static Order save(Order order){
        log.info("Saving order {}", order);
        DB.put(order.orderId(),order);
        log.info("Saved order {}", order);
        return order;
    }

    public static Order findById(String orderId){
        return DB.get(orderId);
    }

    public static List<Order> findAll(){
        return DB.values().stream().toList();
    }
}
