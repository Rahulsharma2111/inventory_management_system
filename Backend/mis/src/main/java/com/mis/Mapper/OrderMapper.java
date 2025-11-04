package com.mis.Mapper;

import com.mis.DTO.Request.OrderRequest;
import com.mis.DTO.Response.OrderResponse;
import com.mis.Entity.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order mapOrderRequestToOrder(OrderRequest orderRequest);

    OrderResponse mapOrderToOrderRequest(Order order1);

    List<OrderResponse> mapOrderListToOrderResponse(List<Order> content);

}
