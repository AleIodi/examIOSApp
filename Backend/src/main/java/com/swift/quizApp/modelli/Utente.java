package com.swift.quizApp.modelli;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "utente")
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(min = 2, max = 50)
    private String username;

    @NotNull
    @JsonIgnore
    private String password;

    @NotNull
    @Size(min = 2, max = 255)
    private String imgPath;

    @NotNull
    @Size(min = 2, max = 50)
    private String email;

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL)
    private List<Quiz> quiz;

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL)
    private List<RispostaUtente> risposteUtente;

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL)
    private List<QuizRisultato> risultatiQuiz;

    public Utente() {}
    public Utente(String username, String password, String imgPath, String email) {
        this.username = username;
        this.password = password;
        this.imgPath = imgPath;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getImgPath() {
        return imgPath;
    }
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
