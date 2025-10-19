package com.swift.quizApp.DAO;

import com.swift.quizApp.modelli.Utente;
import org.springframework.data.repository.CrudRepository;

public interface UtenteDAO extends CrudRepository<Utente, Integer> {
    Utente findByUsername(String username);
    Utente findByEmail(String email);
}
