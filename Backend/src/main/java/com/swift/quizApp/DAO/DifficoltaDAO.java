package com.swift.quizApp.DAO;

import com.swift.quizApp.modelli.Difficolta;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DifficoltaDAO extends CrudRepository<Difficolta, Integer> {
    Optional<Difficolta> findByDescrizione(String difficolta);
}
