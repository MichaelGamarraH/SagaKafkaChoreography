package com.tech.service;

import com.tech.constants.KafkaTopics;
import com.tech.constants.OrderStatus;
import com.tech.events.OrderCreatedEvent;
import com.tech.model.Order;
import com.tech.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public Order createOrder(String productId,int quantity,String userId ){
        String orderId = UUID.randomUUID().toString();
        log.info("Generating order with id {}",orderId);
        Order order = new Order(orderId,productId,quantity, OrderStatus.CREATED,userId);
        OrderRepository.save(order);
        log.info("Generating order created event");

        OrderCreatedEvent orderCreatedEvent =
                new OrderCreatedEvent(orderId,productId,quantity,OrderStatus.CREATED,userId);

        kafkaTemplate.send(KafkaTopics.ORDER_CREATED,orderCreatedEvent);
        log.info("Published order created event: {}", orderCreatedEvent);
        return order;
    }

    public List<Order> findAllOrders(){
        return OrderRepository.findAll();
    }
}
