package com.swift.quizApp.DAO;

import com.swift.quizApp.modelli.QuizRisultato;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface QuizRisultatoDAO extends CrudRepository<QuizRisultato, Integer> {
    @Query("""
        SELECT SUM(r.punteggioTotale)
        FROM QuizRisultato r
        WHERE r.utente.id = :utenteId
          AND r.completatoIl BETWEEN :startDate AND :endDate
    """)
    Float getWeeklyScoreByUtenteId(Integer utenteId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("""
        SELECT COUNT(DISTINCT r.utente.id) + 1
        FROM QuizRisultato r
        WHERE
          (SELECT SUM(r2.punteggioTotale)
           FROM QuizRisultato r2
           WHERE r2.utente.id = r.utente.id
             AND r2.completatoIl BETWEEN :startDate AND :endDate
           GROUP BY r2.utente.id)
          >
          (SELECT SUM(r3.punteggioTotale)
           FROM QuizRisultato r3
           WHERE r3.utente.id = :utenteId
             AND r3.completatoIl BETWEEN :startDate AND :endDate)
    """)
    Integer getWeeklyRankByUtenteId(Integer utenteId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT SUM(r.punteggioTotale) FROM QuizRisultato r WHERE r.utente.id = :utenteId")
    Float getTotalScoreByUtenteId(Integer utenteId);
}
