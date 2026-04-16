package com.example.Order_Service.Service;

import com.example.Order_Service.DTO.ItemDTO;
import com.example.Order_Service.DTO.PollingData;
import com.example.Order_Service.Exception.OrderNotFound;
import com.example.Order_Service.Model.Pooling;
import com.example.Order_Service.Repositery.OrderRepositery;
import com.example.Order_Service.Repositery.OrderUnsuccessfullRepositery;
import com.example.Order_Service.Repositery.PoolingRepositery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepositery orderRepositery;

    @Mock
    private OrderUnsuccessfullRepositery orderUnsuccessfullRepositery;

    @Mock
    private PoolingRepositery poolingRepositery;

    @InjectMocks
    private OrderService orderService;

    @Test
    void getOrderBYuseridReturnsMappedItems() {
        List<Object[]> rows = List.of(
                new Object[]{7L, "Phone", 49999.0, 101L},
                new Object[]{7L, "Case", 999.0, 102L}
        );

        when(orderRepositery.getOrderDetailsByUserId(7L)).thenReturn(rows);

        List<ItemDTO> result = orderService.getOrderBYuserid(7L);

        assertEquals(2, result.size());
        assertEquals(new ItemDTO(7L, "Phone", 49999.0, 101L), result.get(0));
        assertEquals(new ItemDTO(7L, "Case", 999.0, 102L), result.get(1));
    }

    @Test
    void getOrderBYuseridThrowsWhenNoOrdersFound() {
        when(orderRepositery.getOrderDetailsByUserId(9L)).thenReturn(List.of());

        OrderNotFound exception = assertThrows(OrderNotFound.class,
                () -> orderService.getOrderBYuserid(9L));

        assertEquals("Orders for 9Not Found", exception.getMessage());
    }

    @Test
    void poolingDTOReturnsMappedResponse() {
        LocalDateTime pollingDate = LocalDateTime.of(2026, 4, 16, 10, 30);
        Pooling pooling = new Pooling(1L, "Y", pollingDate);

        when(poolingRepositery.findById(1L)).thenReturn(Optional.of(pooling));

        PollingData result = orderService.poolingDTO();

        assertEquals(1L, result.getId());
        assertEquals("Y", result.getPoolingEnable());
        assertEquals(pollingDate, result.getPollingDate());
    }

    @Test
    void poolingDTOThrowsWhenConfigurationMissing() {
        when(poolingRepositery.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.poolingDTO());

        assertEquals("Id not found", exception.getMessage());
    }
}
