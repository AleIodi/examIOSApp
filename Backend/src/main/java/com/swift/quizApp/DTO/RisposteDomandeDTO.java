package com.swift.quizApp.DTO;

import com.swift.quizApp.modelli.Opzione;

import java.util.List;

public class RisposteDomandeDTO {
    private Integer id;
    private String testo;
    private List<Opzione> opzioni;

    public RisposteDomandeDTO(Integer id, String testo, List<Opzione> opzioni) {
        this.id = id;
        this.testo = testo;
        this.opzioni = opzioni;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTesto() { return testo; }
    public void setTesto(String testo) { this.testo = testo; }

    public List<Opzione> getOpzioni() { return opzioni; }
    public void setOpzioni(List<Opzione> opzioni) { this.opzioni = opzioni; }
}
