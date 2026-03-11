package com.buyPhone.mapper;


import com.buyPhone.dto.OrderItemDTO;
import com.buyPhone.entity.OrderItem;

public class OrderItemMapper {

    public OrderItemDTO toDTO(OrderItem item){
        if(item == null) return null;

        return OrderItemDTO.builder()
                .id(item.getId() != null ? item.getId().toString() : null)
                .productId(item.getProduct() != null ? item.getProduct().getId().toString() : null)
                .productId(item.getOrder() != null ? item.getOrder().getId().toString() : null)
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build();
    }

    public OrderItem toEntity(OrderItemDTO dto){
        if(dto == null) return null;

        OrderItem item = new OrderItem();
        item.setId(dto.getId() != null ? java.util.UUID.fromString(dto.getId()) : null);
        item.setQuantity(dto.getQuantity());
        item.setPrice(dto.getPrice());
        // product and order should be set in service
        return item;
    }
}