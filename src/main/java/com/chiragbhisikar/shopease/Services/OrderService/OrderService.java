package com.chiragbhisikar.shopease.Services.OrderService;

import com.chiragbhisikar.shopease.Auth.Service.UserDetailService;
import com.chiragbhisikar.shopease.DTO.OrderDTO;
import com.chiragbhisikar.shopease.DTO.OrderItemDTO;
import com.chiragbhisikar.shopease.ENUM.OrderStatus;
import com.chiragbhisikar.shopease.ENUM.PaymentStatus;
import com.chiragbhisikar.shopease.Exception.NotFoundException;
import com.chiragbhisikar.shopease.Model.*;
import com.chiragbhisikar.shopease.Model.Auth.User;
import com.chiragbhisikar.shopease.Repository.AddressRepository;
import com.chiragbhisikar.shopease.Repository.OrderRepository;
import com.chiragbhisikar.shopease.Response.OrderItemRequest;
import com.chiragbhisikar.shopease.Response.OrderRequest;
import com.chiragbhisikar.shopease.Response.OrderResponse;
import com.chiragbhisikar.shopease.Services.PaymentService.PaymentService;
import com.chiragbhisikar.shopease.Services.ProductService.ProductService;
import com.chiragbhisikar.shopease.Services.ProductVariantService.ProductVariantService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@Service
public class OrderService implements iOrderService {
    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductVariantService productVariantService;

    @Autowired
    PaymentService paymentService;

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest, Principal principal) throws NotFoundException, StripeException {
        User user = (User) userDetailService.loadUserByUsername(principal.getName());
        Address address = addressRepository.findAddressByIdAndUserId(orderRequest.getAddressId(), user.getId())
                .orElseThrow(() -> new NotFoundException("Address not found"));

        Order order = Order.builder().user(user).address(address)
                .expectedDeliveryDate(orderRequest.getExpectedDeliveryDate())
                .paymentMethod(orderRequest.getPaymentMethod())
                .orderStatus(OrderStatus.PENDING).build();

        double totalAmount = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest itemRequest : orderRequest.getOrderItemRequests()) {
            try {
                Product product = productService.findProductById(itemRequest.getProductId());
                ProductVariant productVariant = productVariantService.getProductVariant(itemRequest.getProductVariant());

                int quantity = itemRequest.getQuantity();
                double itemTotal = product.getPrice() * quantity;
                totalAmount += itemTotal;

                OrderItem orderItem = OrderItem.builder()
                        .itemPrice(itemTotal)
                        .product(product)
                        .productVariant(productVariant)
                        .quantity(quantity)
                        .order(order)
                        .build();

                orderItems.add(orderItem);
            } catch (Exception e) {
                throw new RuntimeException("Error while preparing order item", e);
            }
        }
        order.setOrderItemList(orderItems);
        order.setTotalAmount(totalAmount);

        Payment payment = new Payment();
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setOrder(order);
        payment.setPaymentMethod(order.getPaymentMethod());
        payment.setAmount(totalAmount);
        order.setPayment(payment);

        Order savedOrder = orderRepository.save(order);

        OrderResponse orderResponse = OrderResponse.builder()
                .paymentMethod(orderRequest.getPaymentMethod())
                .orderId(order.getId())
                .build();
        if (Objects.equals(orderRequest.getPaymentMethod(), "CARD")) {
            orderResponse.setCredentials(paymentService.createPayment(savedOrder));
        }

        return orderResponse;
    }

    public Map<String, String> updateStatus(String paymentIntentId, String status) {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            if (paymentIntent != null && paymentIntent.getStatus().equals("succeeded")) {
                String orderId = paymentIntent.getMetadata().get("orderId");
                Order order = orderRepository.findById(UUID.fromString(orderId)).orElseThrow(BadRequestException::new);

                //                updating payment
                Payment payment = order.getPayment();
                payment.setPaymentStatus(PaymentStatus.COMPLETED);
                payment.setPaymentMethod(paymentIntent.getPaymentMethod());

                //                updating order
                order.setPaymentMethod(paymentIntent.getPaymentMethod());
                order.setOrderStatus(OrderStatus.IN_PROGRESS);
                order.setPayment(payment);
                Order savedOrder = orderRepository.save(order);

                //                preparing response
                Map<String, String> map = new HashMap<>();
                map.put("orderId", String.valueOf(savedOrder.getId()));
                return map;
            } else {
                throw new IllegalArgumentException("PaymentIntent not found or missing metadata");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("PaymentIntent not found or missing metadata");
        }
    }

    public List<OrderDTO> getOrdersByUser(String name) {
        User user = (User) userDetailService.loadUserByUsername(name);
        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream().map(order -> {
            return OrderDTO.builder()
                    .id(order.getId())
                    .orderDate(order.getOrderDate())
                    .orderStatus(order.getOrderStatus())
                    .shipmentNumber(order.getShipmentTrackingNumber())
                    .address(order.getAddress())
                    .totalAmount(order.getTotalAmount())
                    .orderItemList(getItemDetails(order.getOrderItemList()))
                    .expectedDeliveryDate(order.getExpectedDeliveryDate())
                    .build();
        }).toList();
    }

    private List<OrderItemDTO> getItemDetails(List<OrderItem> orderItemList) {
        return orderItemList.stream().map(orderItem -> {
            return OrderItemDTO.builder()
                    .id(orderItem.getId())
                    .itemPrice(orderItem.getItemPrice())
                    .product(orderItem.getProduct())
                    .productVariant(orderItem.getProductVariant())
                    .quantity(orderItem.getQuantity())
                    .build();
        }).toList();
    }

    public void cancelOrder(UUID id, Principal principal) {
        User user = (User) userDetailService.loadUserByUsername(principal.getName());
        Order order = orderRepository.findById(id).get();
        if (null != order && order.getUser().getId().equals(user.getId())) {
            order.setOrderStatus(OrderStatus.CANCELLED);
            //logic to refund amount
            orderRepository.save(order);
        } else {
            new RuntimeException("Invalid request");
        }

    }

}
