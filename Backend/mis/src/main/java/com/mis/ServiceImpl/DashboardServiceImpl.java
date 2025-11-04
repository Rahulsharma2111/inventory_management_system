package com.mis.ServiceImpl;

import com.mis.DTO.Response.DashboardResponse;
import com.mis.Repository.OrderRepository;
import com.mis.Service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final OrderRepository orderRepository;
    @Override
    public DashboardResponse getDashboardStatistics(Long organisationId) {

        return orderRepository.getDashboardStats(organisationId);
    }
}
