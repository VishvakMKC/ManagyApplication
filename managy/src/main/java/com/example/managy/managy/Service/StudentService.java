package com.example.managy.managy.Service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.managy.managy.Entity.Students;
import com.example.managy.managy.Repository.StudentRepository;


@Service
public class StudentService implements CrudService<Students>{

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Students add(Students t) {
        // TODO Auto-generated method stub
        return studentRepository.save(t);
    }

    @Override
    public void delete(Students t) {
        // TODO Auto-generated method stub
        studentRepository.delete(t);
    }

    @Override
    public Students update(Students t) {
        // TODO Auto-generated method stub
        return studentRepository.save(t);
    }

    @Override
    public List<Students> getList() {
        // TODO Auto-generated method stub
        return studentRepository.findAll();
    }

    public Students findByRegno(String regno)
    {
        return studentRepository.findByRegno(regno);
    }

    @Override
    public Page<Students> findPaginated(int pageNo, int pageSize) {
        // TODO Auto-generated method stub
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return studentRepository.findAll(pageable);
    }

    public List<Students> findByDepartmentCode(String departmentCode){
        return studentRepository.findByDepartmentCode(departmentCode);
    }

    public void deleteAll(List<Students> byDepartmentCode) {
        // TODO Auto-generated method stub
        studentRepository.deleteAll(byDepartmentCode);
    }

}
