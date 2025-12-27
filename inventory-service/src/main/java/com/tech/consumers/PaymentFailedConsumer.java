package com.tech.consumers;

import com.tech.constants.KafkaTopics;
import com.tech.events.PaymentFailedEvent;
import com.tech.model.Inventory;
import com.tech.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentFailedConsumer {

    @KafkaListener(topics = KafkaTopics.PAYMENT_FAILED, groupId = "inventory-group")
    public void handlePaymentFailed(ConsumerRecord<String, PaymentFailedEvent> recordEvent){
        PaymentFailedEvent event = recordEvent.value();
        log.info("Received PaymentFailedEvent: {}", event);
        Inventory inventory = InventoryRepository.getByProductId(event.productId());
        Inventory updatedInventory = new Inventory(inventory.productId(),
                inventory.productName(),
                inventory.quantityInStock() + event.quantity(),
                inventory.price());
        InventoryRepository.save(updatedInventory);
    }
}
