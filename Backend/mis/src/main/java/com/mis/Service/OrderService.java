package com.mis.Service;

import com.mis.DTO.Request.OrderRequest;
import com.mis.DTO.Response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createNewOrder(OrderRequest orderRequest);

    List<OrderResponse> getAllOrder(Long organisationId, int page, int size);

}
