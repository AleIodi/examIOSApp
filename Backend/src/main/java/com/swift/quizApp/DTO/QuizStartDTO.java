package com.swift.quizApp.DTO;

import jakarta.validation.constraints.NotNull;

public class QuizStartDTO {
    @NotNull(message = "Difficulty ID is required")
    private Integer difficoltaId;

    private Integer categoriaId;

    public Integer getDifficoltaId() {
        return difficoltaId;
    }
    public void setDifficoltaId(Integer difficoltaId) {
        this.difficoltaId = difficoltaId;
    }

    public Integer getCategoriaId() {
        return categoriaId;
    }
    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }
}