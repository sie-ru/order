package com.test.order.service;

import com.test.order.client.ClientService;
import com.test.order.dto.SaveOrderDTO;
import com.test.order.dto.GetOrderDTO;
import com.test.order.entity.Category;
import com.test.order.entity.Order;
import com.test.order.exception.ClientNotFoundException;
import com.test.order.exception.OrderNotFoundException;
import com.test.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl service;

    @MockBean
    private OrderRepository repository;

    @MockBean
    private ClientService clientService;

    private List<Order> orderList;
    private List<GetOrderDTO> getOrderDTOList;

    @BeforeEach
    public void setUp() {
        orderList = List.of(
                new Order(1L, "Goods #1", Category.CATEGORY_1, 1L),
                new Order(2L, "Goods #2", Category.CATEGORY_2, 1L),
                new Order(3L, "Goods #3", Category.CATEGORY_3, 1L)
        );

        getOrderDTOList = List.of(
                new GetOrderDTO(1L, "Goods #1", Category.CATEGORY_1),
                new GetOrderDTO(2L, "Goods #2", Category.CATEGORY_2),
                new GetOrderDTO(3L, "Goods #3", Category.CATEGORY_3)
        );
    }

    @Test
    void OrderServiceImpl_getOrdersByClient_ShouldReturnValidData() {
        when(repository.findOrderByClientId(1L)).thenReturn(orderList);

        List<GetOrderDTO> actual = service.getOrdersByClient(1L);

        assertNotNull(actual);
        assertEquals(getOrderDTOList, actual);

        verify(repository, times(1)).findOrderByClientId(1L);
    }

    @Test
    void OrderServiceImpl_getOrdersByClient_ShouldReturnEmptyList_IfOrdersIsEmpty() {
        when(repository.findOrderByClientId(25L)).thenReturn(new ArrayList<>());

        List<GetOrderDTO> actual = service.getOrdersByClient(25L);

        assertTrue(actual.isEmpty());
    }

    @Test
    void OrderServiceImpl_getOrder_ShouldReturnValidData() {
        when(repository.findById(1L)).thenReturn(Optional.of(orderList.get(0)));

        GetOrderDTO actual = service.getOrder(1L);

        assertNotNull(actual);
        assertEquals(getOrderDTOList.get(0).getId(), actual.getId());
        assertEquals(getOrderDTOList.get(0).getCategory(), actual.getCategory());
        assertEquals(getOrderDTOList.get(0).getGoodsName(), actual.getGoodsName());

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void OrderServiceImpl_getOrder_ShouldThrowException() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> service.getOrder(999L));

        verify(repository, times(1)).findById(999L);
    }


    @Test
    void OrderServiceImpl_addOrder_ShouldAddNewOrder_AndReturnOrder() {
        when(clientService.checkIfClientExists(anyLong())).thenReturn(true);

        SaveOrderDTO dto = new SaveOrderDTO();
        dto.setGoodsName("Goods name");
        dto.setCategory(Category.CATEGORY_1);
        dto.setClientId(1L);

        Order savedOrder = mock(Order.class);

        when(repository.save(any(Order.class))).thenReturn(savedOrder);

        Order result = service.addOrder(dto);

        verify(clientService).checkIfClientExists(1L);

        verify(repository).save(argThat(order -> order.getGoodsName().equals("Goods name") &&
                order.getCategory().equals(Category.CATEGORY_1) &&
                order.getClientId() == 1L));

        assertEquals(savedOrder, result);
    }

    @Test
    void OrderServiceImpl_updateOrder_ShouldUpdateOrder_IfPassValidData() {
        Long orderId = 1L;
        SaveOrderDTO dto = new SaveOrderDTO();
        dto.setClientId(2L);
        dto.setGoodsName("New Goods");
        dto.setCategory(Category.CATEGORY_3);

        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setClientId(1L);
        existingOrder.setGoodsName("Old Goods");
        existingOrder.setCategory(Category.CATEGORY_1);

        when(repository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(clientService.checkIfClientExists(dto.getClientId())).thenReturn(true);
        when(repository.save(any(Order.class))).thenReturn(existingOrder);

        Order updatedOrder = service.updateOrder(orderId, dto);

        assertNotNull(updatedOrder);
        assertEquals(dto.getGoodsName(), updatedOrder.getGoodsName());
        assertEquals(dto.getCategory(), updatedOrder.getCategory());
        assertEquals(dto.getClientId(), updatedOrder.getClientId());
    }

    @Test
    void OrderServiceImpl_updateOrder_ShouldThrowException() {
        Long orderId = 1L;
        SaveOrderDTO dto = new SaveOrderDTO();
        dto.setClientId(2L);
        dto.setGoodsName("New Goods");
        dto.setCategory(Category.CATEGORY_3);

        when(repository.findById(orderId)).thenReturn(Optional.empty());
        when(clientService.checkIfClientExists(dto.getClientId())).thenReturn(false);

        assertThrows(ClientNotFoundException.class, () -> service.updateOrder(orderId, dto));
    }

    @Test
    void deleteOrder() {
        Long orderId = 123L;

        service.deleteOrder(orderId);

        verify(repository).deleteById(orderId);
    }
}