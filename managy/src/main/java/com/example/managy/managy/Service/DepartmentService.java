package com.example.managy.managy.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.managy.managy.Entity.Department;
import com.example.managy.managy.Repository.DepartmentRepository;

@Service
public class DepartmentService implements CrudService<Department> {

  @Autowired
  private DepartmentRepository departmentRepository;

  @Override
  public Department add(Department t) {
    // TODO Auto-generated method stub
    return departmentRepository.save(t);
  }

  @Override
  public void delete(Department t) {
    // TODO Auto-generated method stub
    departmentRepository.delete(t);
  }

  @Override
  public Department update(Department t) {
    // TODO Auto-generated method stub
    return departmentRepository.save(t);
  }

  @Override
  public List<Department> getList() {
    // TODO Auto-generated method stub
    return departmentRepository.findAll();
  }

  public List<String> departmentCodes() {
    return departmentRepository.findAllDepartmentCodes();
  }

  public Department findById(Long id) {
    return departmentRepository.findById(id).get();
  }

  public Department findByDepartmentCode(String code) {
    return departmentRepository.findByDepartmentCode(code);
  }

  public List<Department> findByKeyWord(String keyword) {
    // TODO Auto-generated method stub
    return departmentRepository.findByKeyWord(keyword);
  }

  @Override
  public Page<Department> findPaginated(int pageNo, int pageSize) {
    // TODO Auto-generated method stub
    Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
    return departmentRepository.findAll(pageable);
  }
}
