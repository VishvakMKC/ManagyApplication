package com.example.managy.managy.Controller;

import com.example.managy.managy.Entity.Department;
import com.example.managy.managy.Entity.Students;
import com.example.managy.managy.Service.DepartmentService;
import com.example.managy.managy.Service.StudentService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.File;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import javax.sql.rowset.serial.SerialException;

@Controller
public class UserController {

  @GetMapping("/user/home")
  public String userHome() {
    return "user";
  }

  @GetMapping("/login")
  public String loginPage() {
    return "custom_login";
  }

  @Autowired
  private DepartmentService departmentService;

  @Autowired
  private StudentService studentService;


  @GetMapping("/user/createdepartment")
  public String userCreateDepartment(Model model) {
    model.addAttribute("user",true);  
    return "CreateDepartment";
  }

  @PostMapping("/user/createdepartment")
  public String createdepartment(@ModelAttribute Department department) {
    //TODO: process POST request
    System.out.println(department);
    departmentService.add(department);
    return "redirect:/user/departments";
  }

  @GetMapping("/user/departments")
  public String departments(Model model) {
    model.addAttribute("user", true);
    return findDepartmentPaginated(1, model);
    // model.addAttribute("departments", departmentService.getList());
    // return "Departments";
  }

  @GetMapping("/user/departments/{pageno}")
  public String findDepartmentPaginated(
    @PathVariable("pageno") int pageNo,
    Model model
  ) {
    model.addAttribute("user", true);
    int pageSize = 6;
    Page<Department> page = departmentService.findPaginated(pageNo, pageSize);
    List<Department> departments = page.getContent();

    model.addAttribute("departments", departments);
    model.addAttribute("totalPages", page.getTotalPages());
    model.addAttribute("totalItems", page.getTotalElements());
    model.addAttribute("currentPage", pageNo);
    return "Departments";
  }

  @GetMapping("/user/updatedepartment/{code}")
  public String updatedepartment(@PathVariable String code, Model model) {
    Department department2 = departmentService.findByDepartmentCode(code);
    model.addAttribute("user",true);
    model.addAttribute("updept", department2);
    return "UpdateDepartment";
  }

  @PostMapping("/user/updatedepartment/{code}")
  public String updatedepartment(
    @PathVariable String code,
    @ModelAttribute Department department,
    RedirectAttributes redirectAttributes
  ) {
    //TODO: process POST request
    Department department2 = departmentService.findByDepartmentCode(code);
    department2.setDepartmentCode(department.getDepartmentCode());
    department2.setDepartmentName(department.getDepartmentName());
    department2.setEmail(department.getEmail());
    department2.setHod(department.getHod());
    department2.setPhone(department.getPhone());
    departmentService.update(department2);
    redirectAttributes.addFlashAttribute("success", "Successfully updated!!");
    return "redirect:/user/departments";
  }

  @GetMapping("/user/deletedepartment/{code}")
  public String deletedepartment(@PathVariable String code, Model model) {
    //TODO:
    Department department = departmentService.findByDepartmentCode(code);
    model.addAttribute("user",true);
    model.addAttribute("deletedept", department);
    return "DeleteDepartment";
  }

  @GetMapping("/user/viewdepartment/{code}")
  public String viewdepartment(@PathVariable String code, Model model) {
    //TODO:
    Department department = departmentService.findByDepartmentCode(code);
    model.addAttribute("viewdept", department);
    return "ViewDepartment";
  }

  @PostMapping("/user/deletedepartment/{code}")
  public String deletedepartment(
    @PathVariable String code,
    RedirectAttributes redirectAttributes
  ) {
    //TODO:
    Department department = departmentService.findByDepartmentCode(code);
    departmentService.delete(department);
    redirectAttributes.addFlashAttribute(
      "deleted",
      "Successfully deleted the department"
    );
    return "redirect:/user/departments";
  }

  @GetMapping("/user/students")
  public String students(Model model) {
    // List<Students> studentsList = studentService.getList();
    // model.addAttribute("students", studentsList);
    return findStudentPaginated(1, model);
  }

  @GetMapping("/user/students/{pageNo}")
  private String findStudentPaginated(@PathVariable int pageNo, Model model) {
    // TODO Auto-generated method stub
    int pageSize = 10;
    Page<Students> page = studentService.findPaginated(pageNo, pageSize);
    List<Students> students = page.getContent();
    model.addAttribute("user", true);
    model.addAttribute("admin", false);
    model.addAttribute("totalPages", page.getTotalPages());
    model.addAttribute("students", students);
    model.addAttribute("currentPage", pageNo);
    model.addAttribute("totalItems", page.getTotalElements());
    return "Students";
  }

  @GetMapping("/user/createstudent")
  public String createStudent(Model model) {
    model.addAttribute("departments", departmentService.departmentCodes());
    model.addAttribute("user",true);
    return "CreateStudent";
  }

  @PostMapping("/user/createstudent")
  public String createStudent(
    @ModelAttribute Students student,
    @RequestParam("profile") MultipartFile profile
  ) throws IOException, SerialException, SQLException {
    Students student2 = student;
    byte[] image = profile.getBytes();
    Blob images = new javax.sql.rowset.serial.SerialBlob(image);
    student2.setImage(images);
    File file = new File(
      "E:\\SpringBoot\\SpringSecurity\\springsecurity\\src\\main\\resources\\static\\images\\" +
      student.getRegno() +
      "." +
      profile.getContentType().split("/")[1]
    );
    student2.setPicPath(
      "/images/" +
      student.getRegno() +
      "." +
      profile.getContentType().split("/")[1]
    );
    profile.transferTo(file);
    studentService.add(student2);
    return "redirect:/user/students";
  }

  @GetMapping("/user/viewstudent/{regno}")
  public String viewStudent(@PathVariable String regno, Model model) {
    Students student = studentService.findByRegno(regno);
    model.addAttribute("student", student);
    return "ViewStudent";
  }

  @GetMapping("/user/updatestudent/{regno}")
  public String updateStudent(@PathVariable String regno, Model model) {
    Students student = studentService.findByRegno(regno);
    model.addAttribute("user",true);
    model.addAttribute("student", student);
    return "UpdateStudent";
  }

  @GetMapping("/user/deletestudent/{regno}")
  public String deleteStudent(@PathVariable String regno, Model model) {
    Students student = studentService.findByRegno(regno);
    model.addAttribute("student", student);
    return "DeleteStudent";
  }

  @PostMapping("/user/updatestudent/{regno}")
  public String updateStudent(
    @PathVariable String regno,
    @ModelAttribute Students student,
    @RequestParam("profile") MultipartFile profile,
    RedirectAttributes redirectAttributes
  ) throws IOException, SerialException, SQLException {
    Students student2 = studentService.findByRegno(regno);

    if (profile.isEmpty()) {
      student.setImage(student2.getImage());
      student.setPicPath(student2.getPicPath());
    } else {
      byte[] image = profile.getBytes();
      Blob images = new javax.sql.rowset.serial.SerialBlob(image);
      student.setImage(images);
      File file = new File(
        "E:\\SpringBoot\\SpringSecurity\\springsecurity\\src\\main\\resources\\static\\images\\" +
        student.getRegno() +
        "." +
        profile.getContentType().split("/")[1]
      );
      student.setPicPath(
        "/images/" +
        student.getRegno() +
        "." +
        profile.getContentType().split("/")[1]
      );
      profile.transferTo(file);
    }
    studentService.add(student);
    redirectAttributes.addFlashAttribute("success", "Successfully updated!!");

    return "redirect:/user/students";
  }

  @PostMapping("/user/deletestudent/{regno}")
  public String deletestudent(@PathVariable String regno, RedirectAttributes redirectAttributes) {
    //TODO: process POST request
    studentService.delete(studentService.findByRegno(regno));
    redirectAttributes.addFlashAttribute("success", "Successfully deleted!!");

    return "redirect:/user/students";
  }

  @GetMapping("/user/listdepartments")
  public String listdepartments(
    Model model,
    @RequestParam("keyword") String keyword
  ) {
    //TODO: process POST request
    model.addAttribute("user", true);

    model.addAttribute("keyword", keyword);
    model.addAttribute("departments", departmentService.findByKeyWord(keyword));
    return "Departments";
  }

  @GetMapping("/user/liststudents")
  public String listStudents(Model model, @RequestParam("regno") String regno) {
    //TODO: process POST request
    model.addAttribute("user", true);

    model.addAttribute("regno", regno);
    model.addAttribute("students", studentService.findByRegno(regno));
    return "Students";
  }
  @GetMapping("/user/deptstudents/{dept}")
  public String userDeptStudents(@PathVariable String dept, Model model) {
    model.addAttribute("user",true);
    model.addAttribute("students",studentService.findByDepartmentCode(dept));
    return "Students";
  }
  
}


