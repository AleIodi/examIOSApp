package com.swift.quizApp.modelli;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "difficolta")
public class Difficolta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(min = 2, max = 15)
    private String descrizione;

    @NotNull
    private float moltiplicatore;

    @OneToMany(mappedBy = "difficolta", cascade = CascadeType.ALL)
    private List<Domanda> domande;

    public Difficolta() {}
    public Difficolta(Integer id, String descrizione, float moltiplicatore) {
        this.id = id;
        this.descrizione = descrizione;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}
