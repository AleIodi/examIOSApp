package com.swift.quizApp.controller;

import com.swift.quizApp.DAO.UtenteDAO;
import com.swift.quizApp.DTO.UtenteLoginDTO;
import com.swift.quizApp.DTO.UtenteSignUpDTO;
import com.swift.quizApp.modelli.Utente;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UtenteController {
    @Autowired
    UtenteDAO utenteRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${upload.dir}")
    private String uploadDir;

    private String saveProfilePicture(MultipartFile file, String username) throws IOException {
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String filename = username + extension;
        Path filepath = Paths.get(uploadDir, filename);

        Files.copy(file.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody UtenteLoginDTO dto, BindingResult bindingResult, HttpSession sessione) {
        Map<String, Object> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("message", "Validation error");
            response.put("errors", errors);
            return ResponseEntity.badRequest().body(response);
        }

        Utente utenteLoggato = utenteRepo.findByUsername(dto.getUsername());
        if (utenteLoggato == null || !passwordEncoder.matches(dto.getPassword(), utenteLoggato.getPassword())) {
            response.put("message", "Validation error");
            response.put("errors", List.of("Invalid username or password"));
            return ResponseEntity.badRequest().body(response);
        }

        sessione.setAttribute("visitatore_id", utenteLoggato.getId());
        response.put("message", "Login successful");
        response.put("user", utenteLoggato);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/signUp", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> signUp(@Valid @RequestPart("infoSignUp") UtenteSignUpDTO dto, @RequestPart(value = "img", required = false) MultipartFile file, BindingResult bindingResult, HttpSession sessione) throws IOException {
        Map<String, Object> response = new HashMap<>();
        List<String> errors = new ArrayList<>();

        if (bindingResult.hasErrors()) {
            errors.addAll(bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList()));
        }

        if (utenteRepo.findByUsername(dto.getUsername()) != null) {
            errors.add("Username already exists");
        }

        if (utenteRepo.findByEmail(dto.getEmail()) != null) {
            errors.add("Email already exists");
        }

        if (file != null && !file.isEmpty()) {
            if (file.getSize() > 5 * 1024 * 1024) {
                errors.add("Profile picture must be less than 5MB");
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                errors.add("Profile picture must be an image file");
            }
        }

        if (!errors.isEmpty()) {
            response.put("message", "Validation error");
            response.put("errors", errors);
            return ResponseEntity.badRequest().body(response);
        }

        String profilePicPath = "default.webp";
        if (file != null && !file.isEmpty()) {
            try {
                profilePicPath = saveProfilePicture(file, dto.getUsername());
            } catch (IOException e) {
                response.put("message", "Error saving profile picture");
                response.put("errors", List.of(e.getMessage()));
                return ResponseEntity.status(500).body(response);
            }
        }

        Utente newUtente = new Utente();
        newUtente.setUsername(dto.getUsername());
        newUtente.setEmail(dto.getEmail());
        newUtente.setDataNascita(dto.getDataNascita());
        newUtente.setPassword(passwordEncoder.encode(dto.getPassword()));
        newUtente.setImgPath(profilePicPath);

        utenteRepo.save(newUtente);

        sessione.setAttribute("visitatore_id", newUtente.getId());
        response.put("message", "Sign up successful");
        response.put("user", newUtente);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession sessione) {
        Map<String, Object> response = new HashMap<>();

        sessione.invalidate();

        response.put("message", "Logout successful");
        return ResponseEntity.ok(response);
    }
}
