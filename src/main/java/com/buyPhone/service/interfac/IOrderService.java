package com.buyPhone.service.interfac;

import com.buyPhone.dto.OrderDTO;

import java.util.List;
import java.util.UUID;

public interface IOrderService {


        OrderDTO placeOrder(UUID userId);

        OrderDTO getOrder(UUID orderId);

        List<OrderDTO> getUserOrders(UUID userId);

        OrderDTO updateOrderStatus(UUID orderId, String status);


}
