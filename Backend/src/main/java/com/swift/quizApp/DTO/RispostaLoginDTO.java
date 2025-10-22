package com.swift.quizApp.DTO;

import com.swift.quizApp.modelli.Utente;

public class RispostaLoginDTO {
    private Utente utenteLoggato;
    private int rank;
    private float punti;

    public RispostaLoginDTO(Utente utenteLoggato, int rank, float punti) {
        this.utenteLoggato = utenteLoggato;
        this.rank = rank;
        this.punti = punti;
    }

    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }
    public void setUtenteLoggato(Utente utenteLoggato) {
        this.utenteLoggato = utenteLoggato;
    }

    public int getRank() {
        return rank;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }

    public float getPunti() {
        return punti;
    }
    public void setPunti(float punti) {
        this.punti = punti;
    }
}
