package com.swift.quizApp.DAO;

import com.swift.quizApp.modelli.Domanda;
import org.springframework.data.repository.CrudRepository;

public interface DomandaDAO extends CrudRepository<Domanda, Integer> {
}
