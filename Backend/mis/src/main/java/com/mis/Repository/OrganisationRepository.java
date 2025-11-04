package com.mis.Repository;

import com.mis.Entity.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation,Long> {

    boolean existsByRegistrationNumber(String registrationNumber);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByGstNumber(String gstNumber);

    Organisation findByEmail(String email);


    @Modifying
    @Query("UPDATE Organisation o SET o.jwtToken = :jwtToken WHERE o.id = :organisationId")
    int updatePasswordAndToken(@Param("organisationId") Long organisationId, @Param("jwtToken") String jwtToken);

}
