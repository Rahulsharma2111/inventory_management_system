package com.mis.Mapper;

import com.mis.DTO.Request.OrganisationRequest;
import com.mis.DTO.Response.OrganisationResponse;
import com.mis.Entity.Organisation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrganisationMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
//    @Mapping(target = "members", ignore = true)
    Organisation mapOrganisationRequestIntoOrganisation(OrganisationRequest organisationRequest);

//    @Mapping(target = "password", ignore = true)
    OrganisationResponse mapOrganisationIntoOrganisationResponse(Organisation organisation1);
}

