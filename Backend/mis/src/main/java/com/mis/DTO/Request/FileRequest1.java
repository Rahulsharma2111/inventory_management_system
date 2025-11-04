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
public class FileRequest1 {
    @NotNull(message = "Organization ID is required")
    private Long organizationId;

    @NotNull(message = "Files are required")
    private List<SingleFileRequest> fileDetails;

}
