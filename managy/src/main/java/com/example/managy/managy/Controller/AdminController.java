package com.example.managy.managy.Controller;

import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import javax.sql.rowset.serial.SerialException;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.managy.managy.Entity.Department;
import com.example.managy.managy.Entity.Students;
import com.example.managy.managy.Service.DepartmentService;
import com.example.managy.managy.Service.StudentService;

@Controller
public class AdminController {

  @Autowired
  private DepartmentService departmentService;

  @Autowired
  private StudentService studentService;

  @GetMapping("/home")
  public String home() {
    return "Home";
  }

  @GetMapping("/admin/home")
  public String adminHome(Model model) {
    return "admin";
  }

  @GetMapping("/admin/createdepartment")
  public String getMethodName(Model model) {
    model.addAttribute("admin",true);
    model.addAttribute("user",false);
    return "CreateDepartment";
  }

  @PostMapping("/admin/createdepartment")
  public String createdepartment(@ModelAttribute Department department) {
    //TODO: process POST request
    System.out.println(department);
    departmentService.add(department);
    return "redirect:/admin/departments";
  }

  @GetMapping("/admin/departments")
  public String departments(Model model) {
    return findDepartmentPaginated(1,model);
    // model.addAttribute("departments", departmentService.getList());
    // return "Departments";
  }

  @GetMapping("/admin/departments/{pageno}")
  public String findDepartmentPaginated(@PathVariable("pageno") int pageNo, Model model)
  {
    int pageSize = 6;
    Page<Department> page = departmentService.findPaginated(pageNo, pageSize);
    List<Department> departments = page.getContent();
    
    model.addAttribute("admin",true);
    model.addAttribute("user",false);
    model.addAttribute("departments",departments);
    model.addAttribute("totalPages",page.getTotalPages());
    model.addAttribute("totalItems",page.getTotalElements());
    model.addAttribute("currentPage",pageNo);
    return "Departments";
  }

  @GetMapping("/admin/updatedepartment/{code}")
  public String updatedepartment(@PathVariable String code, Model model) {
    Department department2 = departmentService.findByDepartmentCode(code);
    model.addAttribute("admin", true);
    model.addAttribute("user",false);

    model.addAttribute("updept", department2);
    return "UpdateDepartment";
  }

  @PostMapping("/admin/updatedepartment/{code}")
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
    return "redirect:/admin/departments";
  }

  @GetMapping("/admin/deletedepartment/{code}")
  public String deletedepartment(@PathVariable String code, Model model) {
    //TODO:
    Department department = departmentService.findByDepartmentCode(code);
    model.addAttribute("deletedept", department);
    model.addAttribute("admin",true);
    model.addAttribute("user",false);
    return "DeleteDepartment";
  }

  @GetMapping("/admin/viewdepartment/{code}")
  public String viewdepartment(@PathVariable String code, Model model) {
    //TODO:
    Department department = departmentService.findByDepartmentCode(code);
    model.addAttribute("viewdept", department);
    return "ViewDepartment";
  }

  @PostMapping("/admin/deletedepartment/{code}")
  public String deletedepartment(
    @PathVariable String code,
    RedirectAttributes redirectAttributes
  ) {
    //TODO:
    Department department = departmentService.findByDepartmentCode(code);
    departmentService.delete(department);
    studentService.deleteAll(studentService.findByDepartmentCode(code));
    redirectAttributes.addFlashAttribute(
      "deleted",
      "Successfully deleted the department"
    );
    return "redirect:/admin/departments";
  }

  @GetMapping("/admin/students")
  public String students(Model model) {
    // List<Students> studentsList = studentService.getList();
    // model.addAttribute("students", studentsList);
    
    return findStudentPaginated(1, model);
  }

  @GetMapping("/admin/students/{pageNo}")
  private String findStudentPaginated(@PathVariable int pageNo, Model model) {
    // TODO Auto-generated method stub
    int pageSize = 10;
    Page<Students> page = studentService.findPaginated(pageNo, pageSize);
    List<Students> students = page.getContent();
    model.addAttribute("admin",true);
    model.addAttribute("user",false);
    model.addAttribute("totalPages",page.getTotalPages());
    model.addAttribute("students",students);
    model.addAttribute("currentPage",pageNo);
    model.addAttribute("totalItems",page.getTotalElements());
    return "Students";
  }

  @GetMapping("/admin/createstudent")
  public String createStudent(Model model) {
    model.addAttribute("admin",true);
    model.addAttribute("user",false);
    model.addAttribute("departments", departmentService.departmentCodes());
    return "CreateStudent";
  }

  @PostMapping("/admin/createstudent")
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
    return "redirect:/admin/students";
  }

  @GetMapping("/admin/viewstudent/{regno}")
  public String viewStudent(@PathVariable String regno, Model model) {
    Students student = studentService.findByRegno(regno);
    model.addAttribute("student", student);
    return "ViewStudent";
  }

  @GetMapping("/admin/updatestudent/{regno}")
  public String updateStudent(@PathVariable String regno, Model model) {
    Students student = studentService.findByRegno(regno);
    model.addAttribute("admin",true);
    model.addAttribute("user",false);
    model.addAttribute("student", student);
    return "UpdateStudent";
  }

  @GetMapping("/admin/deletestudent/{regno}")
  public String deleteStudent(@PathVariable String regno, Model model) {
    Students student = studentService.findByRegno(regno);
    model.addAttribute("admin",true);
    model.addAttribute("user",false);
    model.addAttribute("student", student);
    return "DeleteStudent";
  }

  @PostMapping("/admin/updatestudent/{regno}")
  public String updateStudent(
    @PathVariable String regno,
    @ModelAttribute Students student,
    @RequestParam("profile") MultipartFile profile, RedirectAttributes redirectAttributes) throws IOException, SerialException, SQLException {
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

    return "redirect:/admin/students";
  }

  @PostMapping("/admin/deletestudent/{regno}")
  public String deletestudent(@PathVariable String regno, RedirectAttributes redirectAttributes) {
    //TODO: process POST request
    studentService.delete(studentService.findByRegno(regno));
    redirectAttributes.addFlashAttribute("deleted", "Successfully deleted!!");

    return "redirect:/admin/students";
  }

  @GetMapping("/admin/listdepartments")
  public String listdepartments(Model model, @RequestParam("keyword") String keyword) {
    //TODO: process POST request
    model.addAttribute("admin", true);
    model.addAttribute("user",false);
    model.addAttribute("keyword",keyword);
    model.addAttribute("departments", departmentService.findByKeyWord(keyword));
    return "Departments";
  }
  @GetMapping("/admin/liststudents")
  public String listStudents(Model model, @RequestParam("regno") String regno) {
    //TODO: process POST request
    model.addAttribute("admin", true);
    model.addAttribute("user",false);
    model.addAttribute("regno",regno);
    model.addAttribute("students", studentService.findByRegno(regno));
    return "Students";
  }
  @GetMapping("/admin/deptstudents/{dept}")
  public String adminDeptStudents(@PathVariable String dept, Model model) {
    model.addAttribute("user",false);
    model.addAttribute("admin",true);
    model.addAttribute("students",studentService.findByDepartmentCode(dept));
    return "Students";
  }
}
