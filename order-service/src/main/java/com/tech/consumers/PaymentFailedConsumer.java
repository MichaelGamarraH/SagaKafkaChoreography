package com.tech.consumers;

import com.tech.constants.KafkaTopics;
import com.tech.constants.OrderStatus;
import com.tech.events.PaymentFailedEvent;
import com.tech.model.Order;
import com.tech.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentFailedConsumer {

    @KafkaListener(topics = KafkaTopics.PAYMENT_FAILED, groupId = "order-group")
    public void handleOrderCreated(ConsumerRecord<String, PaymentFailedEvent> record) {
        PaymentFailedEvent event = record.value();
        log.info("Received PaymentFailedEvent: {}", event);
        Order order = OrderRepository.findById(event.orderId());
        Order updatedOrder = new Order(order.orderId(), order.productId(),
                order.quantity(), OrderStatus.FAILED_PAYMENT, order.userId());
        OrderRepository.save(updatedOrder);
        log.info("All Orders: {}", OrderRepository.findAll());
    }

}
