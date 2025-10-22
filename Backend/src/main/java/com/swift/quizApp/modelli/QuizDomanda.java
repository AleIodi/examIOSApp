package com.swift.quizApp.modelli;

import jakarta.persistence.*;

@Entity
@Table(name = "quiz_domanda")
public class QuizDomanda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn
    private Domanda domanda;

    @ManyToOne
    @JoinColumn
    private Quiz quiz;

    public QuizDomanda() {}
    public QuizDomanda(Quiz quiz, Domanda domanda) {
        this.quiz = quiz;
        this.domanda = domanda;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Domanda getDomanda() {
        return domanda;
    }
    public void setDomanda(Domanda domanda) {
        this.domanda = domanda;
    }

    public Quiz getQuiz() {
        return quiz;
    }
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}
