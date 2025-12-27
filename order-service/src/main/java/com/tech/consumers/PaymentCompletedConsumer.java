package com.tech.consumers;

import com.tech.constants.KafkaTopics;
import com.tech.constants.OrderStatus;
import com.tech.events.PaymentCompletedEvent;
import com.tech.model.Order;
import com.tech.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentCompletedConsumer {

    @KafkaListener(topics = KafkaTopics.PAYMENT_COMPLETED,groupId = "order-group")
    public void handleOrderCreated(ConsumerRecord<String, PaymentCompletedEvent> eventRecord){
        PaymentCompletedEvent event = eventRecord.value();
        log.info("Received PaymentCompletedEvent: {}", event);
        Order order = OrderRepository.findById(event.orderId());
        Order updatedOrder = new Order(order.orderId(),order.productId(),
                order.quantity(), OrderStatus.CONFIRMED, order.userId());

        OrderRepository.save(updatedOrder);
        log.info("All Orders: {}", OrderRepository.findAll());
    }

}
