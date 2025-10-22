package com.swift.quizApp.DAO;

import com.swift.quizApp.modelli.Domanda;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DomandaDAO extends CrudRepository<Domanda, Integer> {
    @Query(value = "SELECT * FROM domanda WHERE difficolta_id = :idDiff AND categoria_id = :categoriaId ORDER BY RAND() LIMIT :numDomande", nativeQuery = true)
    List<Domanda> findRandomDomandeByDifficoltaAndCategoria(int idDiff, int numDomande, Integer categoriaId);

    @Query(value = "SELECT * FROM domanda WHERE difficolta_id = :idDiff ORDER BY RAND() LIMIT :numDomande", nativeQuery = true)
    List<Domanda> findRandomDomandeByDifficolta(int idDiff, int numDomande);
}
