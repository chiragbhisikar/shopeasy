package com.chiragbhisikar.shopease.Controller;

import com.chiragbhisikar.shopease.DTO.OrderDTO;
import com.chiragbhisikar.shopease.Exception.NotFoundException;
import com.chiragbhisikar.shopease.Response.ApiResponse;
import com.chiragbhisikar.shopease.Response.OrderRequest;
import com.chiragbhisikar.shopease.Response.OrderResponse;
import com.chiragbhisikar.shopease.Services.OrderService.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse> createOrder(@RequestBody OrderRequest orderRequest, Principal principal) {
        try {
            OrderResponse orderResponse = orderService.createOrder(orderRequest, principal);

            return new ResponseEntity<>(new ApiResponse("Order Placed Successfully !", orderResponse), HttpStatus.CREATED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("Something Went Wrong ! " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update-payment")
    public ResponseEntity<?> updatePaymentStatus(@RequestBody Map<String, String> request) {
        try {
            Map<String, String> response = orderService.updateStatus(request.get("paymentIntent"), request.get("status"));

            return new ResponseEntity<>(new ApiResponse("Order Updated Successfully !", response), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("Something Went Wrong ! " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable UUID id, Principal principal) {
        try {
            orderService.cancelOrder(id, principal);
            return new ResponseEntity<>(new ApiResponse("Order Cancelled Successfully !", null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("Something Went Wrong ! " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse> getOrderByUser(Principal principal) {
        try {
            List<OrderDTO> orders = orderService.getOrdersByUser(principal.getName());
            return new ResponseEntity<>(new ApiResponse("User Orders Gotten Successfully !", orders), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("Something Went Wrong ! " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}