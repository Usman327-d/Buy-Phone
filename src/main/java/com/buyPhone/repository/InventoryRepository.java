package com.buyPhone.repository;

import com.buyPhone.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

    @Modifying
    @Query("UPDATE Inventory i SET i.quantity = :qty WHERE i.product.id = :pid")
    int updateQuantity(@Param("pid") UUID productId, @Param("qty") Integer quantity);

    // Alternative: Increment/Decrement (Better for sales/restocking)
    @Modifying
    @Query("UPDATE Inventory i SET i.quantity = i.quantity + :change WHERE i.product.id = :pid")
    int adjustQuantity(@Param("pid") UUID productId, @Param("change") Integer change);
}
