package com.swift.quizApp.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UtenteLoginDTO {
        @NotNull
        @Size(min = 1, max = 50)
        private String username;

        @NotNull
        private String password;

        public UtenteLoginDTO() {}
        public UtenteLoginDTO(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
