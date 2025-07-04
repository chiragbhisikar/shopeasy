package com.chiragbhisikar.shopease.Repository;


import com.chiragbhisikar.shopease.Model.Auth.User;
import com.chiragbhisikar.shopease.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUser(User user);
}