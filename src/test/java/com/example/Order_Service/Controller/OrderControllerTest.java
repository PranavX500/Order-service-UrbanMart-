package com.example.Order_Service.Controller;

import com.example.Order_Service.DTO.ItemDTO;
import com.example.Order_Service.DTO.ProductRequestEvent;
import com.example.Order_Service.DTO.RazorpayOrderResponse;
import com.example.Order_Service.Service.OrderConsumer;
import com.example.Order_Service.Service.OrderConsumer2;
import com.example.Order_Service.Service.OrderProducer;
import com.example.Order_Service.Service.OrderProducer2;
import com.example.Order_Service.Service.OrderService;
import com.example.Order_Service.Service.RequestQuantityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderProducer orderProducer;

    @Mock
    private OrderConsumer orderConsumer;

    @Mock
    private OrderProducer2 orderProducer2;

    @Mock
    private OrderConsumer2 orderConsumer2;

    @Mock
    private OrderService orderService;

    @Mock
    private RequestQuantityService requestQuantityService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createOrderStoresRequestPublishesEventAndReturnsRequestId() throws Exception {
        ProductRequestEvent requestEvent = new ProductRequestEvent();
        requestEvent.setRequestId("req-123");
        doNothing().when(requestQuantityService).store(any(ProductRequestEvent.class));
        doNothing().when(orderProducer).sendIds(any(ProductRequestEvent.class));

        mockMvc.perform(post("/Order/Order-Summary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-EMAIL", "user@example.com")
                        .header("X-UserId", "42")
                        .content(objectMapper.writeValueAsString(requestEvent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value("req-123"));

        ArgumentCaptor<ProductRequestEvent> captor = ArgumentCaptor.forClass(ProductRequestEvent.class);
        verify(requestQuantityService).store(captor.capture());
        verify(orderProducer).sendIds(captor.getValue());
        assertEquals("user@example.com", captor.getValue().getEmailId());
        assertEquals(42L, captor.getValue().getUserId());
        assertEquals("req-123", captor.getValue().getRequestId());
    }

    @Test
    void getorderReturnsOrdersFromService() throws Exception {
        when(orderService.getOrderBYuserid(42L)).thenReturn(
                List.of(new ItemDTO(42L, "Phone", 49999.0, 101L))
        );

        mockMvc.perform(get("/Order/GetOrder").header("X-UserId", "42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(42))
                .andExpect(jsonPath("$[0].pname").value("Phone"))
                .andExpect(jsonPath("$[0].price").value(49999.0))
                .andExpect(jsonPath("$[0].productId").value(101));
    }

    @Test
    void getPaymentResultReturnsCachedResponseAndClearsIt() throws Exception {
        RazorpayOrderResponse response = new RazorpayOrderResponse(
                "razor-order-1", 5000, "INR", "receipt-1", 77L, "req-123"
        );
        Map<String, RazorpayOrderResponse> cache = new HashMap<>();
        cache.put("req-123", response);

        when(orderConsumer2.getCache()).thenReturn(cache);

        RazorpayOrderResponse result = orderController.getPaymentResult("req-123");

        assertEquals(response, result);
        assertNull(cache.get("req-123"));
    }
}
