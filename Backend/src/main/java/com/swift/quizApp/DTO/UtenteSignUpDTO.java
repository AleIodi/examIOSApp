package com.swift.quizApp.DTO;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class UtenteSignUpDTO {
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;

    @NotBlank(message = "Email cannot be empty")
    @Pattern(
            regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            message = "Invalid email format"
    )
    private String email;

    @NotNull(message = "Birth date cannot be empty")
    @Past(message = "Birth date must be in the past")
    private LocalDate dataNascita;

    @NotBlank(message = "Password cannot be empty")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!*()_\\-]).{8,}$",
            message = "Invalid Password. Must contain 1 Lowercase, 1 Uppercase, 1 Digit, 1 Special Char and at least 8 characters"
    )
    private String password;

    public UtenteSignUpDTO() {}
    public UtenteSignUpDTO(String username, String email, LocalDate dataNascita, String password) {
        this.username = username;
        this.email = email;
        this.dataNascita = dataNascita;
        this.password = password;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getDataNascita() { return dataNascita; }
    public void setDataNascita(LocalDate dataNascita) { this.dataNascita = dataNascita; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}