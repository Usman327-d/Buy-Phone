package com.buyPhone.service.interfac;

import com.buyPhone.entity.Order;
import com.stripe.exception.StripeException;

public interface IStripeService {

    String createCheckoutSession(Order order) throws StripeException;
}
