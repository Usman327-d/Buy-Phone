package com.buyPhone.service.impl;

import com.buyPhone.entity.Order;
import com.buyPhone.entity.Payment;
import com.buyPhone.enums.OrderStatus;
import com.buyPhone.exception.BadRequestException;
import com.buyPhone.exception.ResourceNotFoundException;
import com.buyPhone.repository.OrderRepository;
import com.buyPhone.repository.PaymentRepository;
import com.buyPhone.service.interfac.IPaymentService;
import com.buyPhone.service.interfac.IStripeService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final IStripeService stripeService;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;


    @Override
    @Transactional
    public void OrderFulfillmentWithPayment(UUID orderId, String transactionId, String method) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found", orderId.toString()));

        // Idempotency: Prevent double processing
        if ("PAID".equals(order.getStatus().name()))
            return;

        order.setStatus(OrderStatus.PAID);

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setTransactionId(transactionId);
        payment.setPaymentMethod(method);
        payment.setPaymentStatus("SUCCESS");
        payment.setPaidAt(LocalDateTime.now());

        paymentRepository.save(payment);
        orderRepository.save(order);
    }

    @Override
    public String processCheckout(UUID orderId) throws StripeException {
        Order order = orderRepository.getReferenceById(orderId);

        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            throw new BadRequestException("Order cannot be paid in its current state: " + order.getStatus());
        }

        return stripeService.createCheckoutSession(order);
    }

    @Override
    public void processWebhook(String payload, String sigHeader) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            if ("checkout.session.completed".equals(event.getType())) {
                Object dataObject = event.getData().getObject();
                String orderIdStr = null;
                String paymentIntentId = null;

                // Handle if the library returns a Session object (your current case)
                if (dataObject instanceof Session session) {
                    orderIdStr = session.getMetadata().get("order_id");
                    paymentIntentId = session.getPaymentIntent();
                }
                // Handle if the library returns a Map (fallback)
                else if (dataObject instanceof Map) {
                    Map<String, Object> dataMap = (Map<String, Object>) dataObject;
                    Map<String, String> metadata = (Map<String, String>) dataMap.get("metadata");
                    if (metadata != null) {
                        orderIdStr = metadata.get("order_id");
                    }
                    paymentIntentId = (String) dataMap.get("payment_intent");
                }

                if (orderIdStr != null) {
                    log.info("Payment Verified! Order ID: {}", orderIdStr);
                    OrderFulfillmentWithPayment(UUID.fromString(orderIdStr), paymentIntentId, "STRIPE");
                } else {
                    log.error("Order ID not found in metadata. Object type: {}", dataObject.getClass().getName());
                }
            } else {
                log.info("Ignored event type: {}", event.getType());
            }

        } catch (Exception e) {
            log.error("Webhook processing failed: {}", e.getMessage());
            // Do not throw exception here, so we return 200 to Stripe
        }
    }

}
