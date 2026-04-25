package com.buyPhone.scheduler;

import com.buyPhone.entity.Order;
import com.buyPhone.entity.OrderItem;
import com.buyPhone.enums.OrderStatus;
import com.buyPhone.repository.InventoryRepository;
import com.buyPhone.repository.OrderRepository;
import com.buyPhone.service.interfac.IInventoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class InventoryCleanupTask {

    private final OrderRepository orderRepository;
    private final IInventoryService inventoryService;

    // Run every minute
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void releaseAbandonedStock() {
        // Find orders older than 10 minutes that are still PENDING
        LocalDateTime timeout = LocalDateTime.now().minusMinutes(10);

        // Batching: Process 50 at a time to keep transactions short
        List<Order> expiredOrders = orderRepository.findExpiredOrdersWithItems(
                OrderStatus.PENDING,
                timeout,
                PageRequest.of(0, 50)
        );

        if (expiredOrders.isEmpty()) {
            return;
        }

        log.info("Janitor: Found {} expired orders to clean up.", expiredOrders.size());

        for (Order order : expiredOrders) {
            try {
                // Return stock to inventory for each item
                for (OrderItem item : order.getOrderItems()) {
                    inventoryService.increaseStock(item.getProduct().getId(), item.getQuantity());
                }

                // Mark order as EXPIRED (Never delete!)
                order.setStatus(OrderStatus.EXPIRED);
                log.debug("Order {} cancelled due to payment timeout.", order.getId());

            } catch (Exception e) {
                log.error("Failed to release stock for order {}: {}", order.getId(), e.getMessage());
                // Transactional ensures this specific order isn't partially processed
            }
        }

        orderRepository.saveAll(expiredOrders);
    }

}
