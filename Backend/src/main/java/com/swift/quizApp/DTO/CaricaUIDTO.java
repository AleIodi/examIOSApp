package com.swift.quizApp.DTO;

import com.swift.quizApp.modelli.Categoria;
import com.swift.quizApp.modelli.Difficolta;

import java.util.List;

public class CaricaUIDTO {
    private List<Difficolta> difficolta;
    private List<Categoria> categorie;

    public CaricaUIDTO(List<Difficolta> difficolta, List<Categoria> categorie) {
        this.difficolta = difficolta;
        this.categorie = categorie;
    }

    public List<Difficolta> getDifficolta() { return difficolta; }
    public void setDifficolta(List<Difficolta> difficolta) { this.difficolta = difficolta; }

    public List<Categoria> getCategorie() { return categorie; }
    public void setCategorie(List<Categoria> categorie) { this.categorie = categorie; }
}