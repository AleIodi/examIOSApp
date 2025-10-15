package com.swift.quizApp.DAO;

import com.swift.quizApp.modelli.Quiz;
import org.springframework.data.repository.CrudRepository;

public interface QuizDAO extends CrudRepository<Quiz, Integer> {
}
