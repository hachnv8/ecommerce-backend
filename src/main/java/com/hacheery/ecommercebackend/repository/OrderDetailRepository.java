package com.hacheery.ecommercebackend.repository;

import com.hacheery.ecommercebackend.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
