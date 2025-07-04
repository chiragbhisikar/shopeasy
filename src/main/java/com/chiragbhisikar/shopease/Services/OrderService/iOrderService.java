package com.chiragbhisikar.shopease.Services.OrderService;

import com.chiragbhisikar.shopease.Exception.NotFoundException;
import com.chiragbhisikar.shopease.Response.OrderRequest;
import com.chiragbhisikar.shopease.Response.OrderResponse;
import com.stripe.exception.StripeException;

import java.security.Principal;
import java.util.Map;

public interface iOrderService {
    public OrderResponse createOrder(OrderRequest orderRequest, Principal principal) throws NotFoundException, StripeException;

    public Map<String, String> updateStatus(String paymentIntentId, String status);
}
