package com.test.order.controller;

import com.test.order.client.ClientService;
import com.test.order.dto.GetOrderDTO;
import com.test.order.dto.SaveOrderDTO;
import com.test.order.entity.Category;
import com.test.order.entity.Order;
import com.test.order.exception.OrderNotFoundException;
import com.test.order.repository.OrderRepository;
import com.test.order.service.OrderService;
import liquibase.util.StringUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ClientService clientService;

    @MockBean
    private OrderRepository repository;

    @MockBean
    private OrderService service;

    @Test
    void getOrdersByClient_ShouldReturnValidJsonResponse() throws Exception {
        Long clientId = 1L;
        List<GetOrderDTO> orders = new ArrayList<>();
        orders.add(new GetOrderDTO(1L, "Goods #1", Category.CATEGORY_1));
        orders.add(new GetOrderDTO(2L, "Goods #2", Category.CATEGORY_2));
        orders.add(new GetOrderDTO(3L, "Goods #3", Category.CATEGORY_3));

        when(service.getOrdersByClient(clientId)).thenReturn(orders);

        mvc.perform(get("/api/order").param("clientId", clientId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].goodsName").value("Goods #1"))
                .andExpect(jsonPath("$[0].category").value("CATEGORY_1"));
    }

    @Test
    void getOrdersByClient_ShouldReturnEmptyList_IfClientHasNoOrders() throws Exception {
        long clientId = 1L;

        mvc.perform(get("/api/order?clientId=5"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON_VALUE),
                        content().json("[]")
                );
    }

    @Test
    void getOrder_ShouldReturnValidJsonResponse() throws Exception {
        long orderId = 1L;
        GetOrderDTO dto = new GetOrderDTO(1L, "Goods #1", Category.CATEGORY_1);
        when(service.getOrder(orderId)).thenReturn(dto);
        mvc.perform(get("/api/order/" + orderId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.goodsName").value("Goods #1"))
                .andExpect(jsonPath("$.category").value("CATEGORY_1"));
    }

    @Test
    void getOrder_ShouldReturn204_IfOrderNotFound() throws Exception {
        long orderId = 999L;
        when(service.getOrder(orderId)).thenThrow(OrderNotFoundException.class);
        mvc.perform(get("/api/order/{orderId}", orderId))
                .andExpectAll(
                        status().isNoContent()
                );
    }

    @Test
    void createOrder_ShouldReturn200() throws Exception {
        SaveOrderDTO dto = new SaveOrderDTO();
        dto.setGoodsName("Goods #55");
        dto.setCategory(Category.CATEGORY_2);
        dto.setClientId(1L);

        Order newOrder = new Order();
        newOrder.setGoodsName("Goods #55");
        newOrder.setCategory(Category.CATEGORY_2);
        newOrder.setClientId(1L);

        when(clientService.checkIfClientExists(1L)).thenReturn(true);
        when(service.addOrder(any(SaveOrderDTO.class))).thenReturn(newOrder);

        mvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"goodsName\": \"Goods #55\",\n" +
                                "    \"category\": 1,\n" +
                                "    \"clientId\": 1\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goodsName").value("Goods #55"))
                .andExpect(jsonPath("$.category").value("CATEGORY_2"))
                .andExpect(jsonPath("$.clientId").value(1));
    }

    @Test
    void createOrder_ShouldReturnBadRequest() throws Exception {
        when(clientService.checkIfClientExists(1L)).thenReturn(true);

        mvc.perform(post("/api/order")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\n" +
                        "    \"goodsName\": " + StringUtil.repeat("a", 200) +",\n" +
                        "    \"category\": 1,\n" +
                        "    \"clientId\": 1\n" +
                        "}"))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @Test
    void updateOrder() throws Exception {
        long orderId = 1L;
        SaveOrderDTO dto = new SaveOrderDTO();
        dto.setGoodsName("Goods #55");
        dto.setCategory(Category.CATEGORY_2);
        dto.setClientId(1L);

        Order updatedOrder = new Order();
        updatedOrder.setGoodsName("Goods #55");
        updatedOrder.setCategory(Category.CATEGORY_2);
        updatedOrder.setClientId(1L);

        when(clientService.checkIfClientExists(1L)).thenReturn(true);
        when(service.updateOrder(any(Long.class), any(SaveOrderDTO.class))).thenReturn(updatedOrder);

        mvc.perform(put("/api/order/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"goodsName\": \"Goods #55\",\n" +
                                "    \"category\": 1,\n" +
                                "    \"clientId\": 1\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goodsName").value("Goods #55"))
                .andExpect(jsonPath("$.category").value("CATEGORY_2"))
                .andExpect(jsonPath("$.clientId").value(1));
    }

    @Test
    void deleteOrder_ShouldDeleteOrderAndReturn200_IfOrderExists() throws Exception {
        Long orderId = 1L;

        when(service.deleteOrder(orderId)).thenReturn(true);

        mvc.perform(delete("/api/order/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}