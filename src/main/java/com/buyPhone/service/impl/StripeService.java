package com.buyPhone.service.impl;

import com.buyPhone.entity.Order;
import com.buyPhone.service.interfac.IStripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StripeService implements IStripeService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${frontend.url}")
    private String frontendUrl;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
        // This forces the library to use the version it was designed for
        Stripe.setConnectTimeout(30000); // 30 seconds
        Stripe.setReadTimeout(60000);
    }


    @Override
    public String createCheckoutSession(Order order) throws StripeException {

            List<SessionCreateParams.LineItem> lineItems = order.getOrderItems().stream()
                    .map(item -> SessionCreateParams.LineItem.builder()
                            .setQuantity((long) item.getQuantity())
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("usd")
                                    .setUnitAmount((long) (item.getPrice() * 100)) // Amount in cents
                                    .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName(item.getProduct().getName())
                                            .addImage(item.getProduct().getImageUrl()) // Cloudinary URL
                                            .build())
                                    .build())
                            .build())
                    .collect(Collectors.toList());

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(frontendUrl + "/payment-success?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(frontendUrl + "/payment-failed")
                    .addAllLineItem(lineItems)
                    // Metadata allows the Webhook to identify the order later
                    .putMetadata("order_id", order.getId().toString())
                    .build();

            Session session = Session.create(params);
            return session.getUrl();

    }
}
