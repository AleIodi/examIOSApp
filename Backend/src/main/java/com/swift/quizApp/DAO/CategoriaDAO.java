package com.swift.quizApp.DAO;

import com.swift.quizApp.modelli.Categoria;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoriaDAO extends CrudRepository<Categoria, Integer> {

    Optional<Categoria> findByNome(String nome);
}
