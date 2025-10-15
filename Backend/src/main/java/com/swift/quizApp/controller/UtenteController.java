package com.swift.quizApp.controller;

import com.swift.quizApp.DAO.UtenteDAO;
import com.swift.quizApp.DTO.UtenteLoginDTO;
import com.swift.quizApp.modelli.Utente;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UtenteController {
    @Autowired
    UtenteDAO utenteRepo;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UtenteLoginDTO dto, HttpSession sessione) {
        Map<String, Object> response = new HashMap<>();
        List<String> errors = new ArrayList<>();

        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            errors.add("È obbligatorio inserire un username");
        }

        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            errors.add("Password non valida. La password non può essere vuota");
        }

        Utente utenteLoggato = utenteRepo.findByUsernameAndPassword(dto.getUsername(), dto.getPassword());
        if (utenteLoggato == null) {
            errors.add("Utente non trovato");
        }

        if (!errors.isEmpty()) {
            response.put("message", "Errore di validazione");
            response.put("errors", errors);
            return ResponseEntity.badRequest().body(response);
        } else {
            response.put("message", "Login eseguito con successo.");
            response.put("user",utenteLoggato);
        }
        sessione.setAttribute("visitatore_id", utenteLoggato.getId());
        return ResponseEntity.ok(response);
    }
}
