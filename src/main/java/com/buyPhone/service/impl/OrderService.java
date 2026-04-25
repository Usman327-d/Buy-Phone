package com.buyPhone.service.impl;

import com.buyPhone.dto.OrderDTO;
import com.buyPhone.dto.OrderItemDTO;
import com.buyPhone.dto.OrderRequestDTO;
import com.buyPhone.entity.*;
import com.buyPhone.enums.OrderStatus;
import com.buyPhone.exception.BadRequestException;
import com.buyPhone.exception.ResourceNotFoundException;
import com.buyPhone.mapper.OrderItemMapper;
import com.buyPhone.mapper.OrderMapper;
import com.buyPhone.repository.OrderRepository;
import com.buyPhone.repository.ProductRepository;
import com.buyPhone.repository.UserRepository;
import com.buyPhone.service.interfac.IOrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {


    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper = new OrderMapper();
    private final InventoryService inventoryService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    @Override
    @Transactional
    public OrderDTO placeOrder(OrderRequestDTO request, UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

        // 2. Build the Shipping Address from the Request
        // This creates a new record for this specific delivery
        Address shippingAddress = new Address();
        shippingAddress.setStreet(request.getStreet());
        shippingAddress.setCity(request.getCity());
        shippingAddress.setState(request.getState());
        shippingAddress.setPostalCode(request.getPostalCode());
        shippingAddress.setCountry(request.getCountry());
        shippingAddress.setIsDefault(request.getIsDefault() != null ? request.getIsDefault() : false);
        shippingAddress.setUser(user);

        // 3. Initialize the Order
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        // 4. Process Order Items and Calculate Total
        double totalAmount = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemDTO itemDto : request.getOrderItems()) {
            // Validation: Ensure the product exists
            Product product = productRepository.findByIdWithLock(UUID.fromString(itemDto.getProductId()))
                    .orElseThrow(() -> new ResourceNotFoundException("Product", itemDto.getProductId()));

            // Security & Business Check: Always use DB price, never Trust DTO price
            // Check stock availability
            if (product.getInventory().getQuantity()< itemDto.getQuantity()) {
                throw new BadRequestException("Insufficient stock for: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order); //  Link to parent for Foreign Key
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPrice(product.getPrice()); // Capture price at time of purchase

            inventoryService.decreaseStock(product.getId(), orderItem.getQuantity());

            totalAmount += (orderItem.getPrice() * orderItem.getQuantity());
            orderItems.add(orderItem);
        }

        // 5. Finalize Order Entity
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        // 6. Persist everything atomically
        // Because Order has CascadeType.ALL for OrderItems, and you've linked the Address,
        // orderRepository.save(order) will persist the Address, Order, and all OrderItems.
        Order savedOrder = orderRepository.save(order);

        // 7. Map to Response DTO
        return orderMapper.toDTO(savedOrder);
    }

    @Override
    public OrderDTO getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId.toString()));
        return orderMapper.toDTO(order);
    }

    @Override
    public List<OrderDTO> getUserOrders(UUID userId) {

       List<Order> orders = orderRepository.findByUserId(userId)
               .orElseThrow(() -> new ResourceNotFoundException("User Orders",  userId.toString()));

        return orders.stream()
                .map(o -> orderMapper.toDTO((Order) o))
                .toList();
    }

    @Override
    public OrderDTO updateOrderStatus(UUID orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId.toString()));
        order.setStatus(OrderStatus.valueOf(status));
        return orderMapper.toDTO(orderRepository.save(order));
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(UUID userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(orderMapper::toDTO)
                .toList();
    }

    @Override
    public List<OrderDTO> getMyOrderHistory(String email) {
        return orderRepository.findByUserEmailOrderByCreatedAtDesc(email)
                .stream()
                .map(orderMapper::toDTO)
                .toList();
    }
}
