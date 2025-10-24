package com.swift.quizApp.controller;

import com.swift.quizApp.DAO.QuizRisultatoDAO;
import com.swift.quizApp.DAO.UtenteDAO;
import com.swift.quizApp.DTO.RispostaLoginDTO;
import com.swift.quizApp.DTO.UtenteLoginDTO;
import com.swift.quizApp.DTO.UtenteSignUpDTO;
import com.swift.quizApp.modelli.Utente;
import com.swift.quizApp.modelli.QuizRisultato;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UtenteController {
    @Autowired
    UtenteDAO utenteRepo;

    @Autowired
    private QuizRisultatoDAO quizRisultatoRepo;

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

        Utente utenteLoggato = utenteRepo.findByUsernameAndPassword(dto.getUsername(), dto.getPassword());
        if (utenteLoggato == null) {
            response.put("message", "Validation error");
            response.put("errors", List.of("Invalid username or password"));
            return ResponseEntity.badRequest().body(response);
        }

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(java.time.DayOfWeek.SUNDAY);
        LocalDateTime weekStart = startOfWeek.atStartOfDay();
        LocalDateTime weekEnd = endOfWeek.atTime(LocalTime.MAX);

        Integer rank = quizRisultatoRepo.getWeeklyRankByUtenteId(utenteLoggato.getId(),weekStart,weekEnd);
        Float punti= quizRisultatoRepo.getWeeklyScoreByUtenteId(utenteLoggato.getId(),weekStart,weekEnd);

        RispostaLoginDTO rispostaLoginDTO=new RispostaLoginDTO(utenteLoggato,rank,punti);

        sessione.setAttribute("user_id", utenteLoggato.getId());
        response.put("message", "Login successful");
        response.put("user", rispostaLoginDTO);
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

        if (dto.getDataNascita() != null) {
            LocalDate today = LocalDate.now();
            int age = Period.between(dto.getDataNascita(), today).getYears();
            if (age < 14) {
                errors.add("You must be at least 14 years old to register");
            }
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
                return ResponseEntity.internalServerError().body(response);
            }
        }

        Utente newUtente = new Utente();
        newUtente.setUsername(dto.getUsername());
        newUtente.setEmail(dto.getEmail());
        newUtente.setDataNascita(dto.getDataNascita());
        newUtente.setPassword(dto.getPassword());
        newUtente.setImgPath(profilePicPath);

        utenteRepo.save(newUtente);

        sessione.setAttribute("user_id", newUtente.getId());
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

    @GetMapping("/getRankScore")
    public ResponseEntity<Map<String, Object>> getRankScore(HttpSession sessione) {
        Map<String, Object> response = new HashMap<>();

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(java.time.DayOfWeek.SUNDAY);
        LocalDateTime weekStart = startOfWeek.atStartOfDay();
        LocalDateTime weekEnd = endOfWeek.atTime(LocalTime.MAX);

        Integer userId = (Integer) sessione.getAttribute("user_id");
        Integer rank = quizRisultatoRepo.getWeeklyRankByUtenteId(userId,weekStart,weekEnd);
        Float punti= quizRisultatoRepo.getWeeklyScoreByUtenteId(userId,weekStart,weekEnd);

        response.put("rankPunti", List.of(rank,punti));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getUserProfile(@PathVariable("user_id") Integer userId) {
        Map<String, Object> response = new HashMap<>();

        Utente utente = utenteRepo.findById(userId).orElse(null);

        if (utente == null) {
            response.put("message", "User not found");
            return ResponseEntity.badRequest().body(response);
        }

        List<QuizRisultato> risultati = quizRisultatoRepo.findByUtenteIdOrderByCompletatoIlDesc(userId);

        int totalQuiz = risultati.size();
        float totalScore = 0;
        int totalCorrette = 0;
        int totalDomande = 0;

        for (QuizRisultato risultato : risultati) {
            totalScore += risultato.getPunteggioTotale();
            totalCorrette += risultato.getRisposteCorrette();
            totalDomande += risultato.getQuiz().getQuizDomande().size();
        }

        float averageScore = totalQuiz > 0 ? totalScore / totalQuiz : 0;
        float accuracyPercentage = totalDomande > 0 ? (float) totalCorrette / totalDomande * 100 : 0;

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(java.time.DayOfWeek.SUNDAY);
        LocalDateTime weekStart = startOfWeek.atStartOfDay();
        LocalDateTime weekEnd = endOfWeek.atTime(LocalTime.MAX);
        Integer currentRank = quizRisultatoRepo.getWeeklyRankByUtenteId(userId,weekStart,weekEnd);
        Float currentScore = quizRisultatoRepo.getWeeklyScoreByUtenteId(userId,weekStart,weekEnd);

        List<Map<String, Object>> quizs = risultati.stream()
                .limit(10)
                .map(r -> {
                    Map<String, Object> quizData = new HashMap<>();
                    quizData.put("quizId", r.getQuiz().getId());
                    quizData.put("completatoIl", r.getCompletatoIl());
                    quizData.put("punteggioTotale", r.getPunteggioTotale());
                    quizData.put("risposteCorrette", r.getRisposteCorrette());
                    quizData.put("totaleDomande", r.getQuiz().getQuizDomande().size());
                    return quizData;
                })
                .collect(Collectors.toList());

        Map<String, Object> profile = new HashMap<>();
        profile.put("userId", utente.getId());
        profile.put("username", utente.getUsername());
        profile.put("email", utente.getEmail());
        profile.put("imgPath", utente.getImgPath());
        profile.put("dataNascita", utente.getDataNascita());

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalQuizzes", totalQuiz);
        statistics.put("averageScore", averageScore);
        statistics.put("totalCorrectAnswers", totalCorrette);
        statistics.put("totalQuestions", totalDomande);
        statistics.put("accuracyPercentage", Math.round(accuracyPercentage * 100) / 100.0);
        statistics.put("currentRank", currentRank);
        statistics.put("currentScore", currentScore);
        statistics.put("lastTenQuizzes", quizs);

        profile.put("statistics", statistics);

        response.put("profile", profile);
        return ResponseEntity.ok(response);
    }
}