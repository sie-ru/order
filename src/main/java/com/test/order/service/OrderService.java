package com.test.order.service;

import com.test.order.dto.SaveOrderDTO;
import com.test.order.dto.GetOrderDTO;
import com.test.order.entity.Order;

import java.util.List;

public interface OrderService {

    List<GetOrderDTO> getOrdersByClient(Long clientId);

    GetOrderDTO getOrder(Long orderId);

    Order addOrder(SaveOrderDTO order);

    Order updateOrder(Long orderId, SaveOrderDTO order);

    Boolean  deleteOrder(Long orderId);
}
