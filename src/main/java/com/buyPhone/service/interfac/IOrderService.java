package com.buyPhone.service.interfac;

import com.buyPhone.dto.OrderDTO;
import com.buyPhone.dto.OrderRequestDTO;
import com.buyPhone.entity.User;

import java.util.List;
import java.util.UUID;

public interface IOrderService {


        OrderDTO placeOrder(OrderRequestDTO orderRequest, UUID userId);

        OrderDTO getOrder(UUID orderId);

        List<OrderDTO> getUserOrders(UUID userId);

        OrderDTO updateOrderStatus(UUID orderId, String status);

        List<OrderDTO> getOrdersByUserId(UUID userId);

        List<OrderDTO> getMyOrderHistory(String email);

}
