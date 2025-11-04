package com.mis.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {

        private Long totalProducts;
        private Long totalOrders;
        private Double totalSalesAmount;
}
