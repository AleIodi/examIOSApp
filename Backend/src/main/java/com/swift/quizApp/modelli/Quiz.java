package com.swift.quizApp.modelli;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.aspectj.weaver.patterns.TypePatternQuestions;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "quiz")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private LocalDateTime dataInizio;

    private LocalDateTime dataFine;

    @ManyToOne
    @JoinColumn
    private Utente utente;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<QuizDomanda> quizDomande;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<RispostaUtente> risposteUtente;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<QuizRisultato> risultati;

    public Quiz() {}

    public Quiz(LocalDateTime dataInizio, LocalDateTime dataFine, Utente utente) {
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.utente = utente;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDataInizio() {
        return dataInizio;
    }
    public void setDataInizio(LocalDateTime dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDateTime getDataFine() {
        return dataFine;
    }
    public void setDataFine(LocalDateTime dataFine) {
        this.dataFine = dataFine;
    }

    public Utente getUtente() {
        return utente;
    }
    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public List<QuizDomanda> getQuizDomande() {
        return quizDomande;
    }
}
