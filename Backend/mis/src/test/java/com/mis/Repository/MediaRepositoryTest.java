//package com.mis.Repository;
//
//import com.mis.Entity.Media;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.TestPropertySource;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//@TestPropertySource(properties = {
//        "spring.test.database.replace=NONE",
//        "spring.datasource.url=jdbc:h2:mem:testdb"
//})
//class MediaRepositoryTest {
//
//    @Autowired
//    private MediaRepository mediaRepository;
//
//    private Media media1;
//    private Media media2;
//    private Media logoMedia;
//
//    @BeforeEach
//    void setUp() {
//
//        mediaRepository.deleteAll();
//
//        media1 = Media.builder()
//                .fileName("document.pdf")
//                .organisationId(1L)
//                .mediaType("application/pdf")
//                .description("Test document")
//                .build();
//
//        media2 = Media.builder()
//                .fileName("image.jpg")
//                .organisationId(1L)
//                .mediaType("image/jpeg")
//                .description("Test image")
//                .build();
//
//        logoMedia = Media.builder()
//                .fileName("logo.png")
//                .organisationId(1L)
//                .mediaType("logo_image")
//                .description("Company logo")
//                .build();
//
//        mediaRepository.saveAll(List.of(media1, media2, logoMedia));
//    }
//
//    @Test
//    @DisplayName("Should find media by organisation ID")
//    void findByOrganisationId_WhenExists_ShouldReturnMedia() {
//
//        Media result = mediaRepository.findByOrganisationId(1L);
//
//        assertNotNull(result);
//        assertEquals(1L, result.getOrganisationId());
//    }
//
//    @Test
//    @DisplayName("Should return null when organisation ID not found")
//    void findByOrganisationId_WhenNotExists_ShouldReturnNull() {
//
//        Media result = mediaRepository.findByOrganisationId(999L);
//
//        assertNull(result);
//    }
//
//    @Test
//    @DisplayName("Should find media by organisation ID and media type")
//    void findByOrganisationIdAndMediaType_WhenExists_ShouldReturnMedia() {
//
//        Media result = mediaRepository.findByOrganisationIdAndMediaType(1L, "logo_image");
//
//        assertNotNull(result);
//        assertEquals(1L, result.getOrganisationId());
//        assertEquals("logo_image", result.getMediaType());
//        assertEquals("logo.png", result.getFileName());
//    }
//
//    @Test
//    @DisplayName("Should return null when organisation ID and media type combination not found")
//    void findByOrganisationIdAndMediaType_WhenNotExists_ShouldReturnNull() {
//
//        Media result = mediaRepository.findByOrganisationIdAndMediaType(1L, "nonexistent_type");
//
//        assertNull(result);
//    }
//
//    @Test
//    @DisplayName("Should find all media by organisation ID")
//    void findAllByOrganisationId_WhenExists_ShouldReturnList() {
//
//        List<Media> result = mediaRepository.findAllByOrganisationId(1L);
//
//        assertNotNull(result);
//        assertEquals(3, result.size()); // Should return all 3 test records
//        assertTrue(result.stream().allMatch(media -> media.getOrganisationId().equals(1L)));
//    }
//
//    @Test
//    @DisplayName("Should return empty list when no media found for organisation ID")
//    void findAllByOrganisationId_WhenNotExists_ShouldReturnEmptyList() {
//
//        List<Media> result = mediaRepository.findAllByOrganisationId(999L);
//
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    @DisplayName("Should find media by organisation ID and file ID")
//    void findByOrganisationIdAndId_WhenExists_ShouldReturnMedia() {
//
//        Media result = mediaRepository.findByOrganisationIdAndId(1L, media1.getId());
//
//        assertNotNull(result);
//        assertEquals(1L, result.getOrganisationId());
//        assertEquals(media1.getId(), result.getId());
//        assertEquals("document.pdf", result.getFileName());
//    }
//
//    @Test
//    @DisplayName("Should return null when organisation ID and file ID combination not found")
//    void findByOrganisationIdAndId_WhenNotExists_ShouldReturnNull() {
//        // Act
//        Media result = mediaRepository.findByOrganisationIdAndId(1L, 999L);
//
//        // Assert
//        assertNull(result);
//    }
//
//    @Test
//    @DisplayName("Should find media by organisation ID excluding specific media type")
//    void findByOrganisationIdAndMediaTypeNot_WhenExists_ShouldReturnFilteredList() {
//        // Act
//        List<Media> result = mediaRepository.findByOrganisationIdAndMediaTypeNot(1L, "logo_image");
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(2, result.size()); // Should exclude the logo
//
//        // Verify no logo files in result
//        boolean hasLogo = result.stream()
//                .anyMatch(media -> "logo_image".equals(media.getMediaType()));
//        assertFalse(hasLogo);
//
//        // Verify other files are included
//        boolean hasDocument = result.stream()
//                .anyMatch(media -> "document.pdf".equals(media.getFileName()));
//        boolean hasImage = result.stream()
//                .anyMatch(media -> "image.jpg".equals(media.getFileName()));
//
//        assertTrue(hasDocument);
//        assertTrue(hasImage);
//    }
//
//    @Test
//    @DisplayName("Should return empty list when no matching media found with exclusion")
//    void findByOrganisationIdAndMediaTypeNot_WhenNoMatches_ShouldReturnEmptyList() {
//        // Act
//        List<Media> result = mediaRepository.findByOrganisationIdAndMediaTypeNot(999L, "logo_image");
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    @DisplayName("Should soft delete logo image by setting file_name to NULL")
//    void isSoftDeletedLogoImage_ShouldUpdateFileNameToNull() {
//        // Arrange - get the saved logo media ID
//        Long logoId = logoMedia.getId();
//
//        // Verify file_name exists before update
//        Media beforeUpdate = mediaRepository.findById(logoId).orElseThrow();
//        assertNotNull(beforeUpdate.getFileName());
//
//        // Act
//        int updatedCount = mediaRepository.isSoftDeletedLogoImage(logoId);
//
//        // Assert
//        assertEquals(1, updatedCount); // Should update 1 record
//
//        // Verify file_name is now NULL
//        Media afterUpdate = mediaRepository.findById(logoId).orElseThrow();
//        assertNull(afterUpdate.getFileName());
//    }
//
//    @Test
//    @DisplayName("Should return 0 when soft deleting non-existent ID")
//    void isSoftDeletedLogoImage_WhenIdNotExists_ShouldReturnZero() {
//        // Act
//        int updatedCount = mediaRepository.isSoftDeletedLogoImage(999L);
//
//        // Assert
//        assertEquals(0, updatedCount); // Should update 0 records
//    }
//}
package com.mis.Repository;

import com.mis.Entity.Media;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MediaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MediaRepository mediaRepository;

    private Media media1, media2, media3;

    @BeforeEach
    void setUp() {

        media1 = Media.builder()
                .fileName("document1.pdf")
                .organisationId(1L)
                .mediaType("application/pdf")
                .description("Test document 1")
                .build();

        media2 = Media.builder()
                .fileName("image1.jpg")
                .organisationId(1L)
                .mediaType("image/jpeg")
                .description("Test image 1")
                .build();

        media3 = Media.builder()
                .fileName("document2.pdf")
                .organisationId(2L)
                .mediaType("application/pdf")
                .description("Test document 2")
                .build();

        media1 = entityManager.persistAndFlush(media1);
        media2 = entityManager.persistAndFlush(media2);
        media3 = entityManager.persistAndFlush(media3);
    }

    @Test
    @DisplayName("find by organisation ID")
    void findByOrganisationId_WhenExists_ShouldReturnMedia() {
        List<Media> result = mediaRepository.findAllByOrganisationId(1L);
        assertNotNull(result);
        assertEquals(1L, result.get(0).getOrganisationId());

    }

    @Test
    @DisplayName("not found")
    void findByOrganisationId_WhenNotExists_ShouldReturnNull() {
        Media result = mediaRepository.findByOrganisationId(999L);
        assertNull(result);
    }

    @Test
    @DisplayName("find organisation ID and file ID")
    void findByOrganisationIdAndId_WhenExists_ShouldReturnMedia() {

        Media result = mediaRepository.findByOrganisationIdAndId(1L, media1.getId());
        assertNotNull(result);
        assertEquals(1L, result.getOrganisationId());
        assertEquals(media1.getId(), result.getId());
        assertEquals("document1.pdf", result.getFileName());
    }

    @Test
    @DisplayName("media not found")
    void findByOrganisationIdAndId_WhenNotExists_ShouldReturnNull() {

        Media result = mediaRepository.findByOrganisationIdAndId(1L, 999L);
        assertNull(result);
    }

    @Test
    @DisplayName(" organisation doesn't find")
    void findByOrganisationIdAndId_WhenOrganisationNotMatch_ShouldReturnNull() {
        Media result = mediaRepository.findByOrganisationIdAndId(999L, media1.getId());
        assertNull(result);
    }
}