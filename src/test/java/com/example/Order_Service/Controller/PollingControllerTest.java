package com.example.Order_Service.Controller;

import com.example.Order_Service.DTO.PollingData;
import com.example.Order_Service.DTO.PoolingDTO;
import com.example.Order_Service.Model.Pooling;
import com.example.Order_Service.Repositery.PoolingRepositery;
import com.example.Order_Service.Service.OrderService;
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

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PollingControllerTest {

    @Mock
    private PoolingRepositery poolingRepositery;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private PollingController pollingController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(pollingController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void updatePollingCreatesNewRecordWhenNoneExists() throws Exception {
        when(poolingRepositery.findPoolingById(1L)).thenReturn(null);
        when(poolingRepositery.save(any(Pooling.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PoolingDTO request = new PoolingDTO("Y");

        mockMvc.perform(post("/admin/polling")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Pooling started"));

        ArgumentCaptor<Pooling> captor = ArgumentCaptor.forClass(Pooling.class);
        verify(poolingRepositery).save(captor.capture());
        assertEquals(1L, captor.getValue().getId());
        assertEquals("Y", captor.getValue().getPoolingEnable());
        assertNotNull(captor.getValue().getPollingDate());
    }

    @Test
    void updatePollingUpdatesExistingRecord() throws Exception {
        Pooling existing = new Pooling(1L, "N", LocalDateTime.of(2026, 4, 16, 8, 0));
        when(poolingRepositery.findPoolingById(1L)).thenReturn(existing);
        when(poolingRepositery.save(any(Pooling.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PoolingDTO request = new PoolingDTO("Y");

        mockMvc.perform(post("/admin/polling")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Pooling started"));

        verify(poolingRepositery).save(existing);
        assertEquals("Y", existing.getPoolingEnable());
        assertNotNull(existing.getPollingDate());
    }

    @Test
    void getPollingDataReturnsServiceResponse() throws Exception {
        LocalDateTime pollingDate = LocalDateTime.of(2026, 4, 16, 9, 15);
        when(orderService.poolingDTO()).thenReturn(new PollingData(1L, "Y", pollingDate));

        mockMvc.perform(get("/admin/GetPolling"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.poolingEnable").value("Y"));
    }
}
