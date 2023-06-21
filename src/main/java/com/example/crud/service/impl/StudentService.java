package com.example.crud.service.impl;

import com.example.crud.model.Student;
import com.example.crud.repository.IStudentRepository;
import com.example.crud.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService implements IStudentService {
    @Autowired
    private IStudentRepository iStudentRepository;

    @Override
    public Page<Student> findAllPage(Pageable pageable) {
        return iStudentRepository.findAll(pageable);
    }

    @Override
    public Iterable<Student> findAll() {
        return iStudentRepository.findAll();
    }

    @Override
    public Student save(Student student) {
        return iStudentRepository.save(student);
    }

    @Override
    public void delete(Long id) {
        iStudentRepository.deleteById(id);
    }

    @Override
    public Optional<Student> findOne(Long id) {
        return iStudentRepository.findById(id);
    }

    @Override
    public List<Student> findAllByName() {
        return null;
    }

    @Override
    public List<Student> sortByAgeAsc() {
        return iStudentRepository.findAllByOrderByAgeAsc();
    }

    @Override
    public List<Student> sortByAgeDesc() {
        return iStudentRepository.findAllByOrderByAgeDesc();
    }

    @Override
    public List<Student> sortByAvgPointAsc() {
        return iStudentRepository.findAllByOrderByAveragePointAsc();
    }

    @Override
    public List<Student> sortByAvgPointDesc() {
        return iStudentRepository.findAllByOrderByAveragePointDesc();
    }
}
