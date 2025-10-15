package com.swift.quizApp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DomandaDTO {
    @JsonProperty("type")
    private String tipo;
    @JsonProperty("difficulty")
    private String difficolta;
    @JsonProperty("category")
    private String categoria;
    @JsonProperty("question")
    private String domada;
    @JsonProperty("correct_answer")
    private String opzioneCorretta;
    @JsonProperty("incorrect_answers")
    private List<String> opzioniSbagliate;

    public String getDifficolta() {
        return difficolta;
    }
    public void setDifficolta(String difficolta) {
        this.difficolta = difficolta;
    }

    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDomada() {
        return domada;
    }
    public void setDomada(String domada) {
        this.domada = domada;
    }

    public String getOpzioneCorretta() {
        return opzioneCorretta;
    }
    public void setOpzioneCorretta(String opzioneCorretta) {
        this.opzioneCorretta = opzioneCorretta;
    }

    public List<String> getOpzioniSbagliate() {
        return opzioniSbagliate;
    }
    public void setOpzioniSbagliate(List<String> opzioniSbagliate) {
        this.opzioniSbagliate = opzioniSbagliate;
    }
}
