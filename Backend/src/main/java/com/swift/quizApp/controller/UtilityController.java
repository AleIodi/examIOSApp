package com.swift.quizApp.controller;

import com.swift.quizApp.DAO.CategoriaDAO;
import com.swift.quizApp.DAO.DifficoltaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UtilityController {
    @Autowired
    CategoriaDAO categoriaRepo;
    @Autowired
    DifficoltaDAO difficoltaRepo;

    @GetMapping("/getCaricaDati")
    public Map<String, Object> getCaricaDati() {
        Map<String, Object> result = new HashMap<>();
        result.put("difficolta", difficoltaRepo.findAll());
        result.put("categorie", categoriaRepo.findAll());
        return result;
    }
}
