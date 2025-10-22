package com.swift.quizApp.modelli;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "domanda")
public class Domanda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(min = 2, max = 500)
    private String testo;

    @ManyToOne
    @JoinColumn
    private Categoria categoria;

    @ManyToOne
    @JoinColumn
    private Difficolta difficolta;

    @OneToMany(mappedBy = "domanda", cascade = CascadeType.ALL)
    private List<Opzione> opzioni;

    @OneToMany(mappedBy = "domanda", cascade = CascadeType.ALL)
    private List<QuizDomanda> quizDomande;

    @OneToMany(mappedBy = "domanda", cascade = CascadeType.ALL)
    private List<RispostaUtente> risposteUtenti;

    public Domanda() {}
    public Domanda(String testo) {
        this.testo = testo;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getTesto() {
        return testo;
    }
    public void setTesto(String testo) {
        this.testo = testo;
    }

    public Categoria getCategoria() {
        return categoria;
    }
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Difficolta getDifficolta() {
        return difficolta;
    }
    public void setDifficolta(Difficolta difficolta) {
        this.difficolta = difficolta;
    }

    public List<Opzione> getOpzioni() {
        return opzioni;
    }
    public void setOpzioni(List<Opzione> opzioni) {
        this.opzioni = opzioni;
    }
}
