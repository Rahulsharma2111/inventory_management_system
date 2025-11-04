package com.mis.Service;

import com.mis.DTO.Response.DashboardResponse;

public interface DashboardService {
    DashboardResponse getDashboardStatistics(Long organisationId);
}
