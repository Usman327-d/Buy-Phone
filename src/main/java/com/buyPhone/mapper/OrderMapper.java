package com.buyPhone.mapper;


import com.buyPhone.dto.OrderDTO;
import com.buyPhone.entity.Order;
import com.buyPhone.enums.OrderStatus;

import java.util.stream.Collectors;

public class OrderMapper {

    public OrderDTO toDTO(Order order){
        if(order == null) return null;

        return OrderDTO.builder()
                .id(order.getId() != null ? order.getId().toString() : null)
                .userId(order.getUser() != null ? order.getUser().getId().toString() : null)
                .totalAmount(order.getTotalAmount())
                .status(String.valueOf(order.getStatus()))
                .createdAt(order.getCreatedAt())
                .orderItems(order.getOrderItems() != null
                        ? order.getOrderItems().stream()
                        .map(oi -> new OrderItemMapper().toDTO(oi))
                        .collect(Collectors.toList())
                        : null)
                .build();
    }

    public Order toEntity(OrderDTO dto){
        if(dto == null) return null;

        Order order = new Order();
        order.setId(dto.getId() != null ? java.util.UUID.fromString(dto.getId()) : null);
        order.setTotalAmount(dto.getTotalAmount());
        order.setStatus(OrderStatus.valueOf(dto.getStatus()));
        // user and items should be set in service
        return order;
    }
}
