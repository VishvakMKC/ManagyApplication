package com.example.managy.managy.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.managy.managy.Entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
  @Query("SELECT d.departmentCode FROM Department d")
  List<String> findAllDepartmentCodes();

  Department findByDepartmentCode(String departmentCode);

  @Query(
    value = "SELECT * FROM Departments d WHERE CONCAT(d.department_name,d.department_code,d.hod,d.email) LIKE %?1%",
    nativeQuery = true
  )
  List<Department> findByKeyWord(String keyword);
}
