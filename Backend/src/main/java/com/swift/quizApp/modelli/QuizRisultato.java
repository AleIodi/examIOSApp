package com.swift.quizApp.modelli;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_risultato")
public class QuizRisultato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn
    private Quiz quiz;

    @ManyToOne
    @JoinColumn
    private Utente utente;

    private Float punteggioTotale;

    private Integer risposteCorrette;

    private LocalDateTime completatoIl;

    public QuizRisultato() {}
    public QuizRisultato(Quiz quiz, Utente utente, Float punteggioTotale, Integer risposteCorrette, LocalDateTime completatoIl) {
        this.quiz = quiz;
        this.utente = utente;
        this.punteggioTotale = punteggioTotale;
        this.risposteCorrette = risposteCorrette;
        this.completatoIl = completatoIl;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Quiz getQuiz() {
        return quiz;
    }
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Utente getUtente() {
        return utente;
    }
    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Float getPunteggioTotale() {
        return punteggioTotale;
    }
    public void setPunteggioTotale(Float punteggioTotale) {
        this.punteggioTotale = punteggioTotale;
    }

    public Integer getRisposteCorrette() {
        return risposteCorrette;
    }
    public void setRisposteCorrette(Integer risposteCorrette) {
        this.risposteCorrette = risposteCorrette;
    }

    public LocalDateTime getCompletatoIl() {
        return completatoIl;
    }
    public void setCompletatoIl(LocalDateTime completatoAt) {
        this.completatoIl = completatoAt;
    }
}