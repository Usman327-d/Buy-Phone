package com.buyPhone.repository;

import com.buyPhone.entity.Order;
import com.buyPhone.enums.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository <Order, UUID> {

    Optional<List<Order>> findByUserId(UUID userId);

    // Optimized: FETCH JOIN gets the items and products in ONE query
    // This prevents the "N+1" performance trap during cleanup
    @Query("SELECT DISTINCT o FROM Order o " +
            "JOIN FETCH o.orderItems oi " +
            "JOIN FETCH oi.product p " +
            "WHERE o.status = :status AND o.createdAt < :expirationTime")
    List<Order> findExpiredOrdersWithItems(
            @Param("status") OrderStatus status,
            @Param("expirationTime") LocalDateTime expirationTime,
            Pageable pageable);

    // For the Admin to see a specific user's history
    List<Order> findByUserIdOrderByCreatedAtDesc(UUID userId);
    // For the Logged-in user to see their own history via Email
    List<Order> findByUserEmailOrderByCreatedAtDesc(String email);
}
