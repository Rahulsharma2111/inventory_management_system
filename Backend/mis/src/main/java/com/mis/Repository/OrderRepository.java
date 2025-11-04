package com.mis.Repository;

import com.mis.DTO.Response.DashboardResponse;
import com.mis.DTO.Response.OrderResponse;
import com.mis.Entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByOrganisationId(Long organisationId,Pageable pageable);

//    @Query(value = "SELECT \n" +
//            "    o.id, " +
//            "    o.name,\n" +
//            "    o.email,\n" +
//            "    o.phone,\n" +
//            "    o.price,\n" +
//            "    o.quantity,\n" +
//            "    p.name AS productName,\n" +
//            "    o.product_id AS productId,\n" +
//            "    o.created_at\n" +
//            "FROM orders o\n" +
//            "JOIN products p ON o.product_id = p.id\n" +
//            "WHERE o.organisation_id=:organisationId ", nativeQuery = true)
//    Page<OrderResponse> findAllOrderByOrgId(Long organisationId, Pageable pageable);

//    @Query(value = "SELECT o.id ,o.name, o.email, o.phone, o.price, o.total_amount AS totalAmount, o.quantity, " +
//            "p.name AS productName, o.product_id AS productId, o.created_at " +
//            "FROM orders o JOIN products p ON o.product_id = p.id WHERE o.organisation_id =:organisationId ",
//            countQuery = "SELECT COUNT(o.id) FROM orders o JOIN products p ON o.product_id = p.id WHERE o.organisation_id =:organisationId ", nativeQuery = true)
//    Page<OrderResponse> findAllOrderByOrgId(Long organisationId, Pageable pageable);

    @Query("SELECT new com.mis.DTO.Response.OrderResponse(" +
            "o.id, o.name, o.email, o.phone, o.price, o.quantity, " +
            "p.name, o.totalAmount, o.productId, o.createdAt) " +
            "FROM Order o JOIN o.product p " +
            "WHERE o.organisationId = :organisationId")
    Page<OrderResponse> findAllOrderByOrgId(@Param("organisationId") Long organisationId, Pageable pageable);


//    @Query(value = "SELECT new com.mis.DTO.Response.OrderResponse(" +
//            "o.id, o.name, o.email, o.phone, o.price, o.quantity, p.name, o.total_amount, o.product_id, o.created_at) " +
//            "FROM Order o JOIN Product p ON o.product.id = p.id " +
//            "WHERE o.organisationId = :organisationId")
//    Page<OrderResponse> findAllOrderByOrgId(@Param("organisationId") Long organisationId, Pageable pageable);

//    @Query(value = "SELECT o.id, o.name, o.email, o.phone, o.price, o.total_amount AS totalAmount, o.quantity, " +
//            "p.name AS productName, o.product_id AS productId, o.created_at " +
//            "FROM orders o JOIN products p ON o.product_id = p.id " +
//            "WHERE o.organisation_id = :organisationId",
//            nativeQuery = true)
//    Page<Object[]> findAllOrderByOrgIdNative(@Param("organisationId") Long organisationId, Pageable pageable);

    @Query("""
        SELECT new com.mis.DTO.Response.DashboardResponse(
            (SELECT COUNT(p) FROM Product p WHERE p.organisationId = :orgId AND p.deletedAt IS NULL),
            (SELECT COUNT(o) FROM Order o WHERE o.organisationId = :orgId),
            COALESCE( SUM(o.totalAmount), 0)
        )
        FROM Order o
        WHERE o.organisationId = :orgId
    """)
    DashboardResponse getDashboardStats(@Param("orgId") Long organisationId);

}
