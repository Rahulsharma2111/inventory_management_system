package com.mis.Controller;

import com.mis.ApiResponse.CustomResponse;
import com.mis.DTO.Response.DashboardResponse;
import com.mis.Service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/{organisationId}")
    private ResponseEntity<?> getDashboardStatistics(@PathVariable("organisationId") Long organisationId){
        try {
            DashboardResponse dashboardResponse=dashboardService.getDashboardStatistics(organisationId);
            return CustomResponse.ok("Data fetch successfully",dashboardResponse);
        } catch (Exception e) {
            return  CustomResponse.badRequest("Failed to fetch statistics");
        }
    }
}
