package com.example.Order_Service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestEvent {
    private List<ItemQuantityDTO> items;

    private String RequestId;
    private String emailId;
    private Long UserId;


}

