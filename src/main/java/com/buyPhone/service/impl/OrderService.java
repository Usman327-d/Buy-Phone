package com.buyPhone.service.impl;

import com.buyPhone.dto.OrderDTO;
import com.buyPhone.entity.Order;
import com.buyPhone.entity.OrderItem;
import com.buyPhone.enums.OrderStatus;
import com.buyPhone.exception.BadRequestException;
import com.buyPhone.exception.ResourceNotFoundException;
import com.buyPhone.mapper.OrderMapper;
import com.buyPhone.repository.OrderRepository;
import com.buyPhone.service.interfac.IOrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {


    private final OrderRepository orderRepository;
    private final OrderMapper mapper = new OrderMapper();
    private final InventoryService inventoryService;


    @Override
    @Transactional
    public OrderDTO placeOrder(UUID userId) {

        Order order = (Order) orderRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", userId.toString()));

        if(order.getOrderItems().isEmpty())
            throw new BadRequestException("Cart is empty");

        Order order1 = new Order();
        order.setUser(order.getUser());
        order.setStatus(OrderStatus.PENDING);

        double total = 0;
        for(OrderItem item : order.getOrderItems()){
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getProduct().getPrice() * item.getQuantity());
            orderItem.setOrder(order);

            inventoryService.decreaseStock(item.getProduct().getId(), item.getQuantity());
            order.getOrderItems().add(orderItem);

            total += orderItem.getPrice();
        }
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        return mapper.toDTO(savedOrder);
    }

    @Override
    public OrderDTO getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId.toString()));
        return mapper.toDTO(order);
    }

    @Override
    public List<OrderDTO> getUserOrders(UUID userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(o -> mapper.toDTO((Order) o))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO updateOrderStatus(UUID orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId.toString()));
        order.setStatus(OrderStatus.valueOf(status));
        return mapper.toDTO(orderRepository.save(order));
    }
}
