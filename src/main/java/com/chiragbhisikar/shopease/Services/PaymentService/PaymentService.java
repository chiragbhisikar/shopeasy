package com.chiragbhisikar.shopease.Services.PaymentService;

import com.chiragbhisikar.shopease.Model.Auth.User;
import com.chiragbhisikar.shopease.Model.Order;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PaymentService implements iPaymentService {
    public Map<String, String> createPayment(Order order) throws StripeException {
        User user = order.getUser();
        Map<String, String> metaData = new HashMap<>();
        metaData.put("orderId", order.getId().toString());
        PaymentIntentCreateParams paymentIntentCreateParams = PaymentIntentCreateParams.builder()
                .setAmount((long) (order.getTotalAmount() * 100)) // USD to INR
                .setCurrency("inr")//INR currency
                .putAllMetadata(metaData)
                .setDescription(order.getUser().getId() + " " + order.getUser().getFirstName() + " " + order.getUser().getLastName())
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build()
                )
                .build();
        PaymentIntent paymentIntent = PaymentIntent.create(paymentIntentCreateParams);
        Map<String, String> map = new HashMap<>();
        map.put("client_secret", paymentIntent.getClientSecret());

        return map;
    }
}
