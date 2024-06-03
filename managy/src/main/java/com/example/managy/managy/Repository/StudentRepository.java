package com.example.managy.managy.Repository;

import java.io.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.managy.managy.Entity.Students;

import java.util.List;


@Repository
public interface StudentRepository extends JpaRepository<Students, String> {
    Students findByRegno(String regno);
    List<Students> findByDepartmentCode(String departmentCode);
}
