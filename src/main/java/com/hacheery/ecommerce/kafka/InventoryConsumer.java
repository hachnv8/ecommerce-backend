package com.hacheery.ecommerce.kafka;

import com.hacheery.ecommerce.event.InventoryReservedEvent;
import com.hacheery.ecommerce.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryConsumer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "order-events", groupId = "inventory-service-group")
    public void consumeOrderCreated(OrderCreatedEvent event) {
        System.out.println("📥 InventoryService nhận order-created: " + event);

        // Giả lập kiểm tra tồn kho (ở đây cho qua hết)
        boolean inStock = true;

        if (inStock) {
            InventoryReservedEvent reservedEvent =
                    new InventoryReservedEvent(event.getOrderId(), true, "Stock reserved successfully");
            kafkaTemplate.send("inventory-events", reservedEvent);
            System.out.println("📤 InventoryService gửi inventory-reserved: " + reservedEvent);
        } else {
            InventoryReservedEvent failedEvent =
                    new InventoryReservedEvent(event.getOrderId(), false, "Stock not available");
            kafkaTemplate.send("inventory-events", failedEvent);
            System.out.println("📤 InventoryService gửi inventory-failed: " + failedEvent);
        }
    }
}
