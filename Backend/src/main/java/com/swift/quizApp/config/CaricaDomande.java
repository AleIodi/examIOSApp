package com.swift.quizApp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swift.quizApp.DAO.*;
import com.swift.quizApp.DTO.DomandaDTO;
import com.swift.quizApp.modelli.Categoria;
import com.swift.quizApp.modelli.Difficolta;
import com.swift.quizApp.modelli.Domanda;
import com.swift.quizApp.modelli.Opzione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class CaricaDomande implements CommandLineRunner {
    @Autowired
    private CategoriaDAO categoriaRepo;
    @Autowired
    private DifficoltaDAO difficoltaRepo;
    @Autowired
    private DomandaDAO domandaRepo;
    @Autowired
    private OpzioneDAO opzioneRepo;
    private ObjectMapper objectMapper=new ObjectMapper();

    @Override
    public void run(String... args) throws Exception {
        if (domandaRepo.count() > 0) {
            System.out.println("DB già carico");
            return;
        }

        InputStream inputStream = getClass().getResourceAsStream("/data/db.json");
        DomandaDTO[] domande = objectMapper.readValue(inputStream, DomandaDTO[].class);

        for (DomandaDTO q : domande) {
            Categoria categoria = categoriaRepo.findByNome(q.getCategoria())
                    .orElseGet(() -> categoriaRepo.save(new Categoria(q.getCategoria())));

            Difficolta difficolta = difficoltaRepo.findByDescrizione(q.getDifficolta())
                    .orElseGet(() -> difficoltaRepo.save(new Difficolta(q.getDifficolta(), getMoltiplicatore(q.getDifficolta()))));

            Domanda domanda = new Domanda();
            domanda.setTesto(q.getDomada());
            domanda.setCategoria(categoria);
            domanda.setDifficolta(difficolta);
            domandaRepo.save(domanda);

            Opzione correct = new Opzione();
            correct.setTesto(q.getOpzioneCorretta());
            correct.setCorretta(true);
            correct.setDomanda(domanda);
            opzioneRepo.save(correct);

            for (String s : q.getOpzioniSbagliate()) {
                Opzione o = new Opzione();
                o.setTesto(s);
                o.setCorretta(false);
                o.setDomanda(domanda);
                opzioneRepo.save(o);
            }
        }
        System.out.println("Caricamento completato");
    }

    private float getMoltiplicatore(String diff) {
        return switch (diff.toLowerCase()) {
            case "easy" -> 1.0f;
            case "medium" -> 1.5f;
            case "hard" -> 2.0f;
            default -> 1.0f;
        };
    }
}
