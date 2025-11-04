package com.mis.Repository;

import com.mis.Entity.Media;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MediaRepository extends JpaRepository<Media,Long> {

    Media findByOrganisationId(Long organisationId);

    Media findByOrganisationIdAndMediaType(Long organisationId, String logoImage);

    @Modifying
    @Transactional
    @Query(value = "UPDATE media SET file_name=NULL WHERE id=:id",nativeQuery = true)
    int isSoftDeletedLogoImage(@Param("id") Long id);

    List<Media> findAllByOrganisationId(Long orgId);

//    Media findByOrganisationIdAndId(Long fileId, Long id);

    Media findByOrganisationIdAndId(Long organisationId, Long fileId);

    List<Media> findByOrganisationIdAndMediaTypeNot(Long orgId, String logoImage);


}
