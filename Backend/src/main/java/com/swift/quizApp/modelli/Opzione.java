package com.swift.quizApp.modelli;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "opzione")
public class Opzione {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(min = 2, max = 50)
    private String testo;

    @NotNull
    private boolean corretta;

    @ManyToOne
    @JoinColumn
    private Domanda domanda;

    @OneToMany(mappedBy = "opzione", cascade = CascadeType.ALL)
    private List<RispostaUtente> RispostaUtente;

    public Opzione() {}
    public Opzione(String testo, boolean corretta) {
        this.testo = testo;
        this.corretta = corretta;
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

    public boolean isCorretta() {
        return corretta;
    }
    public void setCorretta(boolean corretta) {
        this.corretta = corretta;
    }

    public Domanda getDomanda() {
        return domanda;
    }
    public void setDomanda(Domanda domanda) {
        this.domanda = domanda;
    }
}
