package com.swift.quizApp.DTO;

import jakarta.validation.constraints.NotNull;

public class RispostaDTO {
    @NotNull(message = "Question ID is required")
    private Integer domandaId;

    @NotNull(message = "Selected option ID is required")
    private Integer opzioneId;

    public Integer getDomandaId() {
        return domandaId;
    }

    public void setDomandaId(Integer domandaId) {
        this.domandaId = domandaId;
    }

    public Integer getOpzioneId() {
        return opzioneId;
    }

    public void setOpzioneId(Integer opzioneId) {
        this.opzioneId = opzioneId;
    }
}
