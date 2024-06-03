package com.example.managy.managy.Entity;

import java.io.File;
import java.sql.Blob;

import org.hibernate.annotations.Collate;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Columns;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "students")
public class Students {

  @Id
  @Column(nullable = false, unique = true)
  private String regno;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private int year;
  @Column(nullable = false)
  private String email;
  @Column(nullable = false)
  private String phone;
  @Column(nullable = false)
  private String departmentCode;
  private String picPath;
  private Blob image;

}
