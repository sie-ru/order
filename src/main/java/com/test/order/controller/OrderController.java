package com.test.order.controller;

import com.test.order.dto.GetOrderDTO;
import com.test.order.dto.SaveOrderDTO;
import com.test.order.entity.Order;
import com.test.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Order", description = "For working with orders")
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Get orders by client")
    @GetMapping
    public ResponseEntity<List<GetOrderDTO>> getOrdersByClient(@RequestParam("clientId") Long clientId) {
        List<GetOrderDTO> orders = orderService.getOrdersByClient(clientId);
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @Operation(summary = "Get order")
    @GetMapping("/{orderId}")
    public ResponseEntity<GetOrderDTO> getOrder(@PathVariable("orderId") Long orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrder(orderId));
    }

    @Operation(summary = "Create new order")
    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody SaveOrderDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.addOrder(dto));
    }

    @Operation(summary = "Update order")
    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(@Valid @RequestBody SaveOrderDTO dto,
                                             @PathVariable("orderId") Long orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateOrder(orderId, dto));
    }

    @Operation(summary = "Delete order")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Boolean> deleteOrder(@PathVariable("orderId") Long orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.deleteOrder(orderId));
    }
}
