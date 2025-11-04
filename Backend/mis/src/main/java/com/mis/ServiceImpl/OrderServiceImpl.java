package com.mis.ServiceImpl;

import com.mis.DTO.Request.OrderRequest;
import com.mis.DTO.Response.OrderResponse;
import com.mis.Entity.Order;
import com.mis.Entity.Product;
import com.mis.Exception.ResourceNotFoundException;
import com.mis.Mapper.OrderMapper;
import com.mis.Repository.OrderRepository;
import com.mis.Repository.ProductRepository;
import com.mis.Service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderResponse createNewOrder(OrderRequest orderRequest) {
        Product product=productRepository.findByIdAndOrganisationId(orderRequest.getProductId(),orderRequest.getOrganisationId());
        if (product==null){
            throw new ResourceNotFoundException("Product not found");
        }

        if (product.getStockQuantity()<orderRequest.getQuantity()){
            throw new RuntimeException("You have "+product.getStockQuantity()+" quantity");
        }
        Order order=orderMapper.mapOrderRequestToOrder(orderRequest);
        try {

            Double totalAmount=orderRequest.getQuantity()*orderRequest.getPrice();
            order.setTotalAmount(totalAmount);
            Order order1 = orderRepository.save(order);

            productRepository.updateProductSellIteam(orderRequest.getProductId(), orderRequest.getQuantity());

            return orderMapper.mapOrderToOrderRequest(order1);
        } catch (Exception e) {
            throw new RuntimeException("Order not created");
        }
    }

    @Override
    public List<OrderResponse> getAllOrder(Long organisationId, int page, int size) {
        Pageable pageable= PageRequest.of(page-1,size, Sort.by("id").descending());
        Page<OrderResponse> orderPage=orderRepository.findAllOrderByOrgId(organisationId,pageable);
        System.out.println(orderPage.getContent());
        return orderPage.getContent();

    }
}
