package com.example.managy.managy.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "departments")    
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 200)
    private String departmentName;
    @Column(nullable = false, unique = true, length = 20)
    private String departmentCode;
    @Column(nullable = false, length = 150)
    private String hod;
    @Column(nullable = false, length = 200)
    private String email;
    @Column(nullable = false, length = 25)
    private String phone;
}
