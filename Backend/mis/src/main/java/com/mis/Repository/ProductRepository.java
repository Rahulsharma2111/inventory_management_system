package com.mis.Repository;

import com.mis.Entity.Product;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findAllByOrganisationId(long l);

    @Modifying
    @Transactional
    @Query(value = "update products set stock_quantity=stock_quantity - :quantity where id=:productId",nativeQuery = true)
    void updateProductSellIteam(Long productId, Integer quantity);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.deletedAt = :date WHERE p.id = :productId")
    int deleteProductByProductId(@Param("productId") Long productId, @Param("date") Date currentDate);

    List<Product> findAllByOrganisationIdAndDeletedAtIsNull(Long organisationId);

    Product findByNameAndOrganisationId(String name,Long OrganisationId);

    Product findByIdAndOrganisationId( Long productId,Long organisationId);

    List<Product> findAllByNameAndOrganisationId( String name, Long organisationId);
}
