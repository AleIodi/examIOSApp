package com.swift.quizApp.DAO;

import com.swift.quizApp.modelli.QuizDomanda;
import org.springframework.data.repository.CrudRepository;

public interface QuizDomandaDAO extends CrudRepository<QuizDomanda, Integer> {
}
