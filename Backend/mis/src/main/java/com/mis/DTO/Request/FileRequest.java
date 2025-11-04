package com.mis.DTO.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileRequest {
    @NotNull(message = "file is required")
    private List<MultipartFile> files;
    @NotNull(message = "Owner is required")
    private Long organizationId;

//    @NotNull(message = "description is required")
//    private String description;
}
