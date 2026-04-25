package com.buyPhone.service.interfac;

import com.stripe.exception.StripeException;

import java.util.UUID;

public interface IPaymentService {

    void OrderFulfillmentWithPayment(UUID orderId, String transactionId, String method);

    String processCheckout(UUID orderId) throws StripeException;

    void processWebhook(String payload, String sigHeader);
}
