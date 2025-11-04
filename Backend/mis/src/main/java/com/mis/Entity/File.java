//package com.mis.Entity;
//
//package com.mis.Entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import java.util.Date;
//
//@Entity
//@Table(name = "media")
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//public class File {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "file_name")
//    private String fileName;
//
//    @Column(name = "organisation_id")
//    private Long organisationId;
//
//    @Column(name = "media_type")
//    private String mediaType;
//
//    @CreationTimestamp
//    @Column(name = "created_at")
//    private Date createdAt;
//
//    @UpdateTimestamp
//    @Column(name = "updated_at")
//    private Date updatedAt;
//
//    @Column(name = "deleted_at")
//    private String deletedAt;
//
//}
