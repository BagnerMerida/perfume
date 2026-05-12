package com.ab.perfume.repository;

import com.ab.perfume.entity.Order;
import com.ab.perfume.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatus(OrderStatus status);
    List<Order> findByPhone(String phone);

}
