package com.tech.consumers;

import com.tech.constants.KafkaTopics;
import com.tech.events.PaymentCompletedEvent;
import com.tech.events.PaymentFailedEvent;
import com.tech.events.StockConfirmedEvent;
import com.tech.model.Transaction;
import com.tech.repository.BankBalanceRepository;
import com.tech.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentTransactionConsumer {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    @KafkaListener(topics = KafkaTopics.STOCK_CONFIRMED, groupId = "payment-group")
    public void handleOrderCreated(ConsumerRecord<String,
                StockConfirmedEvent> record) {
        StockConfirmedEvent event = record.value();
        log.info("Received StockConfirmed: {}", event);
        String transactionId = UUID.randomUUID().toString();
        boolean transactionSuccess = Boolean.FALSE;
        if (BankBalanceRepository.verifyBalanceAndPay(event.userId(),
                event.paymentAmount())) {
            transactionSuccess = Boolean.TRUE;
        }
        Transaction transaction = new Transaction(transactionId, event.orderId(),
                transactionSuccess, event.userId());
        TransactionRepository.save(transaction);
        if (transactionSuccess) {
            PaymentCompletedEvent paymentCompletedEvent =
                    new PaymentCompletedEvent(event.orderId(), transactionId,
                            transactionSuccess, event.userId());
            kafkaTemplate.send(KafkaTopics.PAYMENT_COMPLETED, paymentCompletedEvent);
            log.info("Sent PaymentCompletedEvent: {}", paymentCompletedEvent);
        } else {
            PaymentFailedEvent paymentFailedEvent = new PaymentFailedEvent(event.orderId(), transactionId,
                    transactionSuccess, event.userId(), event.productId(),
                    event.quantity(),
                    "Transaction Failed, " +
                            "Please try again later.");
            kafkaTemplate.send(KafkaTopics.PAYMENT_FAILED, paymentFailedEvent);
            log.info("Sent PaymentFailedEvent: {}", paymentFailedEvent);
        }
    }

}
