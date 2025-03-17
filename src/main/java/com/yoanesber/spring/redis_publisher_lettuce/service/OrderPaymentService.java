package com.yoanesber.spring.redis_publisher_lettuce.service;

import com.yoanesber.spring.redis_publisher_lettuce.dto.CreateOrderPaymentRequestDTO;
import com.yoanesber.spring.redis_publisher_lettuce.entity.OrderPayment;

public interface OrderPaymentService {
    // Create a new OrderPayment record.
    OrderPayment createOrderPayment(CreateOrderPaymentRequestDTO orderPaymentDTO);
}
