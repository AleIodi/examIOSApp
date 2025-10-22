package com.swift.quizApp.DTO;

import java.util.List;

public class QuizStartRispostaDTO {
    private Integer quizId;
    private List<RisposteDomandeDTO> domande;

    public QuizStartRispostaDTO(Integer quizId, List<RisposteDomandeDTO> domande) {
        this.quizId = quizId;
        this.domande = domande;
    }

    public Integer getQuizId() { return quizId; }
    public void setQuizId(Integer quizId) { this.quizId = quizId; }

    public List<RisposteDomandeDTO> getDomande() { return domande; }
    public void setDomande(List<RisposteDomandeDTO> domande) { this.domande = domande; }
}
