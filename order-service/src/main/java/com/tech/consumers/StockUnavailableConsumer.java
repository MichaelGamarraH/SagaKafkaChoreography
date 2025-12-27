package com.tech.consumers;

import com.tech.constants.KafkaTopics;
import com.tech.constants.OrderStatus;
import com.tech.events.StockUnavailableEvent;
import com.tech.model.Order;
import com.tech.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

@Component
@Slf4j
public class StockUnavailableConsumer {
    @KafkaListener(topics = KafkaTopics.STOCK_UNAVAILABLE, groupId = "order-group")
    public void handleOrderCreated(ConsumerRecord<String, StockUnavailableEvent> record) {
        StockUnavailableEvent event = record.value();
        log.info("Received StockUnavailableEvent: {}", event);
        Order order = OrderRepository.findById(event.orderId());
        Order updatedOrder = new Order(order.orderId(), order.productId(),
                order.quantity(), OrderStatus.FAILED_STOCK_UNAVAILABLE,
                order.userId());
        OrderRepository.save(updatedOrder);
        log.info("All Orders: {}", OrderRepository.findAll());
    }
}
