package com.edison.order.model;

import lombok.Data;

@Data
public class OrderInfo {
    private String orderId;
    private String name;
    private String price;
}