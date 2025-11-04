package com.mis.Controller;

import com.mis.ApiResponse.CustomResponse;
import com.mis.DTO.Request.OrderRequest;
import com.mis.DTO.Response.OrderResponse;
import com.mis.Service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    private ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequest orderRequest){
        try {
            OrderResponse orderResponse=orderService.createNewOrder(orderRequest);
            return CustomResponse.created("Order created successfully",orderResponse);
        } catch (Exception e) {
            System.out.println(e);
            return CustomResponse.badRequest(e.getMessage());
        }
    }

    @GetMapping("/{organisationId}")
    private ResponseEntity<?> getAllOrder(@PathVariable Long organisationId,
                                          @RequestParam(name = "page",defaultValue = "1") int page,
                                          @RequestParam(name = "size", defaultValue = "10") int size){
        try {
            List<OrderResponse> orderResponse=orderService.getAllOrder(organisationId,page,size);
            return CustomResponse.ok("Data fetch successfully",orderResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return CustomResponse.badRequest("Fail to fetched");
        }
    }
}
