package com.swift.quizApp.controller;

import com.swift.quizApp.DAO.*;
import com.swift.quizApp.DTO.*;
import com.swift.quizApp.modelli.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quiz")
public class QuizController {
    public static final int PUNTI=100;

    @Autowired
    private UtenteDAO utenteRepo;

    @Autowired
    private DifficoltaDAO difficoltaRepo;

    @Autowired
    private CategoriaDAO categoriaRepo;

    @Autowired
    private DomandaDAO domandaRepo;

    @Autowired
    private QuizDAO quizRepo;

    @Autowired
    private QuizDomandaDAO quizDomandaRepo;

    @Autowired
    private RispostaUtenteDAO rispostaUtenteRepo;

    @Autowired
    private QuizRisultatoDAO quizRisultatoRepo;

    @Autowired
    private OpzioneDAO opzioneRepo;

    private ResponseEntity<Map<String, Object>> validationErrors(BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        List<String> errors = bindingResult.getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());
        response.put("message", "Validation error");
        response.put("errors", errors);
        return ResponseEntity.badRequest().body(response);
    }

    private Utente getCurrentUser(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        return utenteRepo.findById(userId).orElse(null);
    }

    private ResponseEntity<Map<String, Object>> nonAutorizzato() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Unauthorized");
        response.put("errors", List.of("This quiz does not belong to you"));
        return ResponseEntity.status(403).body(response);
    }

    private boolean quizDellUtente(Quiz quiz, Integer userId) {
        return quiz.getUtente().getId().equals(userId);
    }

    @GetMapping("/caricaUI")
    public ResponseEntity<Map<String, Object>> caricaUI() {
        Map<String, Object> response = new HashMap<>();

        List<Difficolta> difficolta = (List<Difficolta>) difficoltaRepo.findAll();
        List<Categoria> categorie = (List<Categoria>) categoriaRepo.findAll();

        CaricaUIDTO dashboard = new CaricaUIDTO(difficolta, categorie);

        response.put("caricaUI", dashboard);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startQuiz(@Valid @RequestBody QuizStartDTO dto, BindingResult bindingResult, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            return validationErrors(bindingResult);
        }

        Utente utente = getCurrentUser(session);
        if (utente == null) {
            response.put("message", "User not found");
            return ResponseEntity.badRequest().body(response);
        }

        int numFacili, numMedie, numDifficili;
        switch (dto.getDifficoltaId()) {
            case 1:
                numFacili = 12;
                numMedie = 6;
                numDifficili = 2;
                break;
            case 2:
                numFacili = 5;
                numMedie = 10;
                numDifficili = 5;
                break;
            case 3:
                numFacili = 2;
                numMedie = 6;
                numDifficili = 12;
                break;
            default:
                response.put("message", "Difficulty not found");
                return ResponseEntity.badRequest().body(response);
        }

        List<Domanda> domandeFacili;
        List<Domanda> domandeMedie;
        List<Domanda> domandeDifficili;

        if (dto.getCategoriaId() != null) {
            domandeFacili = domandaRepo.findRandomDomandeByDifficoltaAndCategoria(1, numFacili, dto.getCategoriaId());
            domandeMedie = domandaRepo.findRandomDomandeByDifficoltaAndCategoria(2, numMedie, dto.getCategoriaId());
            domandeDifficili = domandaRepo.findRandomDomandeByDifficoltaAndCategoria(3, numDifficili, dto.getCategoriaId());
        } else {
            domandeFacili = domandaRepo.findRandomDomandeByDifficolta(1, numFacili);
            domandeMedie = domandaRepo.findRandomDomandeByDifficolta(2, numMedie);
            domandeDifficili = domandaRepo.findRandomDomandeByDifficolta(3, numDifficili);
        }
        List<Domanda> domande = new ArrayList<>();
        domande.addAll(domandeFacili);
        domande.addAll(domandeMedie);
        domande.addAll(domandeDifficili);

        Collections.shuffle(domande);

        if (domande.isEmpty()) {
            response.put("message", "No questions found or not enough questions for the selected criteria");
            return ResponseEntity.badRequest().body(response);
        }

        Quiz quiz = new Quiz();
        quiz.setUtente(utente);
        quiz.setDataInizio(LocalDateTime.now());
        quizRepo.save(quiz);

        for (Domanda domanda : domande) {
            QuizDomanda quizDomanda = new QuizDomanda(quiz, domanda);
            quizDomandaRepo.save(quizDomanda);
        }

        List<RisposteDomandeDTO> domandeDTO = domande.stream()
                .map(d -> new RisposteDomandeDTO(d.getId(), d.getTesto(), d.getOpzioni()))
                .collect(Collectors.toList());

        QuizStartRispostaDTO quizResponse = new QuizStartRispostaDTO(quiz.getId(), domandeDTO);

        response.put("message", "Quiz started successfully");
        response.put("quiz", quizResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{quiz_id}/answer")
    public ResponseEntity<Map<String, Object>> submitAnswer(@PathVariable("quiz_id") Integer quizId, @Valid @RequestBody RispostaDTO dto, BindingResult bindingResult, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            return validationErrors(bindingResult);
        }

        Utente utente = getCurrentUser(session);
        if (utente == null) {
            response.put("message", "User not found");
            return ResponseEntity.badRequest().body(response);
        }

        Quiz quiz = quizRepo.findById(quizId).orElse(null);
        Domanda domanda = domandaRepo.findById(dto.getDomandaId()).orElse(null);
        Opzione opzione = opzioneRepo.findById(dto.getOpzioneId()).orElse(null);

        if (quiz == null || domanda == null || opzione == null) {
            response.put("message", "Invalid data");
            response.put("errors", List.of("quiz, question, or option not found"));
            return ResponseEntity.badRequest().body(response);
        }

        if (!quizDellUtente(quiz, utente.getId())) {
            return nonAutorizzato();
        }

        RispostaUtente risposta = new RispostaUtente();
        risposta.setUtente(utente);
        risposta.setQuiz(quiz);
        risposta.setDomanda(domanda);
        risposta.setOpzione(opzione);
        rispostaUtenteRepo.save(risposta);

        response.put("message", "Answer submitted successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{quiz_id}/complete")
    public ResponseEntity<Map<String, Object>> completeQuiz(@PathVariable("quiz_id") Integer quizId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        Utente utente = getCurrentUser(session);
        if (utente == null) {
            response.put("message", "User not found");
            return ResponseEntity.badRequest().body(response);
        }

        Quiz quiz = quizRepo.findById(quizId).orElse(null);

        if (quiz == null) {
            response.put("message", "Quiz not found");
            return ResponseEntity.badRequest().body(response);
        }

        if (!quizDellUtente(quiz, utente.getId())) {
            return nonAutorizzato();
        }

        List<RispostaUtente> risposte = rispostaUtenteRepo.findByQuizId(quizId);
        int esatte = 0;
        List<Map<String, Object>> listaRisposte = new ArrayList<>();

        for (RispostaUtente risposta : risposte) {
            boolean isCorrect = risposta.getOpzione().isCorretta();
            if (isCorrect) {
                esatte++;
            }

            Map<String, Object> dettRisposta = new HashMap<>();
            dettRisposta.put("domandaId", risposta.getDomanda().getId());
            dettRisposta.put("domandaTesto", risposta.getDomanda().getTesto());
            dettRisposta.put("opzioneSelezionata", risposta.getOpzione().getTesto());
            dettRisposta.put("corretta", isCorrect);

            String correctAnswerText = risposta.getDomanda().getOpzioni().stream()
                    .filter(Opzione::isCorretta)
                    .map(Opzione::getTesto)
                    .findFirst()
                    .orElse("");
            dettRisposta.put("rispostaCorretta", correctAnswerText);

            listaRisposte.add(dettRisposta);
        }

        float multiplier = 1.0f;
        if (!quiz.getQuizDomande().isEmpty()) {
            Domanda firstDomanda = quiz.getQuizDomande().get(0).getDomanda();
            if (firstDomanda.getDifficolta() != null) {
                multiplier = firstDomanda.getDifficolta().getMoltiplicatore();
            }
        }
        float totalScore = (esatte * multiplier) * PUNTI;

        quiz.setDataFine(LocalDateTime.now());
        quizRepo.save(quiz);

        QuizRisultato risultato = new QuizRisultato();
        risultato.setQuiz(quiz);
        risultato.setUtente(quiz.getUtente());
        risultato.setCompletatoIl(LocalDateTime.now());
        risultato.setPunteggioTotale(totalScore);
        risultato.setRisposteCorrette(esatte);
        quizRisultatoRepo.save(risultato);

        Map<String, Object> risultatoData = new HashMap<>();
        risultatoData.put("quizId", quizId);
        risultatoData.put("risposteCorrette", esatte);
        risultatoData.put("totaleDomande", risposte.size());
        risultatoData.put("punteggioTotale", totalScore);
        risultatoData.put("moltiplicatore", multiplier);
        risultatoData.put("dettaglioRisposte", listaRisposte);

        response.put("message", "Quiz completed successfully");
        response.put("risultato", risultatoData);
        return ResponseEntity.ok(response);
    }
}