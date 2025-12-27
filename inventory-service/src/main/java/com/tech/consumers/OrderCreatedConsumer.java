package com.tech.consumers;

import com.tech.constants.KafkaTopics;
import com.tech.events.OrderCreatedEvent;
import com.tech.events.StockConfirmedEvent;
import com.tech.events.StockUnavailableEvent;
import com.tech.model.Inventory;
import com.tech.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCreatedConsumer {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    @KafkaListener(topics = KafkaTopics.ORDER_CREATED,groupId = "inventory-group")
    public void handleOrderCreated(ConsumerRecord<String, OrderCreatedEvent> recordEvent){
        OrderCreatedEvent event = recordEvent.value();
        log.info("Received OrderCreatedEvent: {}", event);
        int quantity = InventoryRepository.getByProductId(event.productId()).quantityInStock();

        if(quantity<event.quantity()){
            StockUnavailableEvent stockUnavailableEvent =
                    new StockUnavailableEvent(event.productId(),Boolean.FALSE,event.orderId());

            kafkaTemplate.send(KafkaTopics.STOCK_UNAVAILABLE,stockUnavailableEvent);
            log.info("Sent StockUnavailableEvent: {}", stockUnavailableEvent);
        }else{
            Inventory inventory = InventoryRepository.getByProductId(event.productId());
            Inventory updatedInventory = new Inventory(inventory.productId(), inventory.productName(),
                    quantity- event.quantity(), inventory.price());

            InventoryRepository.save(updatedInventory);

            StockConfirmedEvent stockConfirmedEvent = new StockConfirmedEvent(inventory.productId(),
                    Boolean.TRUE,event.orderId(), event.userId(), event.quantity()* inventory.price(),
                    event.quantity());
            kafkaTemplate.send(KafkaTopics.STOCK_CONFIRMED, stockConfirmedEvent);
            log.info("Sent StockConfirmedEvent: {}", stockConfirmedEvent);
        }
    }

}
