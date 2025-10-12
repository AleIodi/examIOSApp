package com.swift.quizApp.modelli;

import jakarta.persistence.*;

@Entity
@Table(name = "risposta_utente")
public class RispostaUtente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn
    private Utente utente;

    @ManyToOne
    @JoinColumn
    private Quiz quiz;

    @ManyToOne
    @JoinColumn
    private Domanda domanda;

    @ManyToOne
    @JoinColumn
    private Opzione opzione;

    public  RispostaUtente() {}
    public RispostaUtente(Utente utente, Quiz quiz, Domanda domanda, Opzione opzione) {
        this.utente = utente;
        this.quiz = quiz;
        this.domanda = domanda;
        this.opzione = opzione;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Utente getUtente() {
        return utente;
    }
    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Quiz getQuiz() {
        return quiz;
    }
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Domanda getDomanda() {
        return domanda;
    }
    public void setDomanda(Domanda domanda) {
        this.domanda = domanda;
    }

    public Opzione getOpzione() {
        return opzione;
    }
    public void setOpzione(Opzione opzione) {
        this.opzione = opzione;
    }
}
