package com.example.crud.controller;

import com.example.crud.model.Classes;
import com.example.crud.model.Student;
import com.example.crud.service.IClassesService;
import com.example.crud.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private IStudentService studentService;

    @Autowired
    private IClassesService classesService;

    @ModelAttribute("classes")
    private Iterable<Classes> getClasses() {
        return classesService.findAll();
    }

    @Value("${path-upload}")
    private String upload;

    @GetMapping
    public ModelAndView listStudents() {
        ModelAndView modelAndView = new ModelAndView("/student/list");
        modelAndView.addObject("students", studentService.findAll());
        return modelAndView;
    }

    @GetMapping("/page")
    public ModelAndView pageStudents(@PageableDefault(value = 3) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("/student/page");
        modelAndView.addObject("students", studentService.findAllPage(pageable));
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView createPage() {
        ModelAndView modelAndView = new ModelAndView("/student/create");
        modelAndView.addObject("student", new Student());
        return modelAndView;
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Student student,
                         RedirectAttributes redirect) {
        MultipartFile image = student.getImage();
        try {
            if(!image.isEmpty()) {
                FileCopyUtils.copy(image.getBytes(), new File(upload + image.getOriginalFilename()));
            }
        } catch (IOException io) {
            io.printStackTrace();
            redirect.addFlashAttribute("message", "Error exist!");
            return "redirect:/students";
        }
        student.setCardPhoto(image.getOriginalFilename());
        Student studentCreate = studentService.save(student);
        if (studentCreate != null) {
            Classes classes = student.getClasses();
            classes.setQuantity(classes.getQuantity() + 1);
            classesService.save(classes);
        }
        redirect.addFlashAttribute("message", "Create successfully!");
        return "redirect:/students";
    }

    @GetMapping("/update/{id}")
    public ModelAndView updatePage(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/student/update");
        Optional<Student> studentOptional = studentService.findOne(id);
        if(studentOptional.isPresent()) {
            modelAndView.addObject("student", studentOptional.get());
        } else {
            modelAndView.setViewName("/student/list");
            modelAndView.addObject("students", studentService.findAll());
        }
        return modelAndView;
    }

    @PostMapping("/update/{id}")
    public String update(@ModelAttribute Student student,
                         @PathVariable Long id,
                         RedirectAttributes redirect) {
        Optional<Student> studentOptional = studentService.findOne(id);
        if(studentOptional.isPresent()) {
            MultipartFile image = student.getImage();
            boolean check = image.isEmpty();
            try {
                if(!check) {
                    FileCopyUtils.copy(image.getBytes(), new File(upload + image.getOriginalFilename()));
                }
            } catch (IOException io) {
                io.printStackTrace();
                redirect.addAttribute("message", "Error exist!");
                return "redirect:/students";
            }
            if(!check) {
                student.setCardPhoto(image.getOriginalFilename());
            } else {
                student.setCardPhoto(studentOptional.get().getCardPhoto());
            }
            student.setId(id);
            studentService.save(student);
        }
        redirect.addFlashAttribute("message", "Update successfully!");
        return "redirect:/students";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         RedirectAttributes redirect) {
        Optional<Student> studentOptional = studentService.findOne(id);
        if(studentOptional.isPresent()) {
            studentService.delete(id);
            Classes classes = studentOptional.get().getClasses();
            classes.setQuantity(classes.getQuantity() - 1);
            classesService.save(classes);
        }
        redirect.addFlashAttribute("message", "Delete successfully!");
        return "redirect:/students";
    }

    @GetMapping("/sort_age_asc")
    public ModelAndView listStudentsByAgeAsc() {
        ModelAndView modelAndView = new ModelAndView("/student/list");
        modelAndView.addObject("students", studentService.sortByAgeAsc());
        return modelAndView;
    }

    @GetMapping("/sort_age_desc")
    public ModelAndView listStudentsByAgeDesc() {
        ModelAndView modelAndView = new ModelAndView("/student/list");
        modelAndView.addObject("students", studentService.sortByAgeDesc());
        return modelAndView;
    }

    @GetMapping("/sort_avg_asc")
    public ModelAndView listStudentsByAvgAsc() {
        ModelAndView modelAndView = new ModelAndView("/student/list");
        modelAndView.addObject("students", studentService.sortByAvgPointAsc());
        return modelAndView;
    }

    @GetMapping("/sort_avg_desc")
    public ModelAndView listStudentsByAvgDesc() {
        ModelAndView modelAndView = new ModelAndView("/student/list");
        modelAndView.addObject("students", studentService.sortByAvgPointDesc());
        return modelAndView;
    }
}
