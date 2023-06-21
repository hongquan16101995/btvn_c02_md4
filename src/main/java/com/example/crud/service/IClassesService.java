package com.example.crud.service;

import com.example.crud.dto.ClassesDTO;
import com.example.crud.model.Classes;

import java.util.List;

public interface IClassesService extends IGenerateService<Classes> {
    List<Classes> sortByQuantityAsc();

    List<Classes> sortByQuantityDesc();

    List<ClassesDTO> findAllByAvgPoint();
}
