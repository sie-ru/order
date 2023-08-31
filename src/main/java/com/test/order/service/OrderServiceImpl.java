package com.test.order.service;

import com.test.order.client.ClientService;
import com.test.order.dto.SaveOrderDTO;
import com.test.order.dto.GetOrderDTO;
import com.test.order.entity.Order;
import com.test.order.exception.ClientNotFoundException;
import com.test.order.exception.OrderNotFoundException;
import com.test.order.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final ClientService serviceClient;

    private final OrderRepository orderRepository;

    private final ModelMapper mapper;

    Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl(ClientService serviceClient, OrderRepository orderRepository, ModelMapper mapper) {
        this.serviceClient = serviceClient;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    @Override
    public List<GetOrderDTO> getOrdersByClient(Long clientId) {
        List<Order> orders = orderRepository.findOrderByClientId(clientId);
        if(!orders.isEmpty()) {
            return orders.stream()
                    .map(order -> mapper.map(order, GetOrderDTO.class))
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    @Override
    public GetOrderDTO getOrder(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);

        if(order.isPresent()) {
            return mapper.map(order.get(), GetOrderDTO.class);
        }

        throw new OrderNotFoundException("Order with id: " + orderId + " not found");
    }

    @Override
    public Order addOrder(SaveOrderDTO dto) {
        Order newOrder = new Order();
        Boolean clientExists = serviceClient.checkIfClientExists(dto.getClientId());

        if(!clientExists) {
            throw new ClientNotFoundException("Client with `id`: " + dto.getClientId() + " does not exist");
        }

        newOrder.setGoodsName(dto.getGoodsName());
        newOrder.setCategory(dto.getCategory());
        newOrder.setClientId(dto.getClientId());

        return orderRepository.save(newOrder);
    }

    @Transactional
    @Override
    public Order updateOrder(Long orderId, SaveOrderDTO dto) {
        Optional<Order> existingOrder = orderRepository.findById(orderId);

        if(!serviceClient.checkIfClientExists(dto.getClientId())) {
            throw new ClientNotFoundException("Client with `id`: " + dto.getClientId() + " does not exist");
        }

        if(existingOrder.isPresent()) {
            Order orderToUpdate = existingOrder.get();

            orderToUpdate.setGoodsName(dto.getGoodsName());
            orderToUpdate.setCategory(dto.getCategory());
            orderToUpdate.setClientId(dto.getClientId());

            return orderRepository.save(orderToUpdate);
        }

        return null;
    }

    @Override
    public Boolean deleteOrder(Long orderId) {
        try {
            orderRepository.deleteById(orderId);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
