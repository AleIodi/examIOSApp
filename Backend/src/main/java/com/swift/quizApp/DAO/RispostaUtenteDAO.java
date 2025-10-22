package com.swift.quizApp.DAO;

import com.swift.quizApp.modelli.RispostaUtente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RispostaUtenteDAO extends CrudRepository<RispostaUtente, Integer> {
    List<RispostaUtente> findByQuizId(Integer quizId);
}
