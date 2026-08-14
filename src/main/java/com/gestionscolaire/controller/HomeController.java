package com.gestionscolaire.controller;

import com.gestionscolaire.repository.CoursRepository;
import com.gestionscolaire.repository.EtudiantRepository;
import com.gestionscolaire.repository.InscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final EtudiantRepository etudiantRepository;
    private final CoursRepository coursRepository;
    private final InscriptionRepository inscriptionRepository;

    @GetMapping("/")
    public String accueil(Model model) {
        model.addAttribute("nbEtudiants", etudiantRepository.count());
        model.addAttribute("nbCours", coursRepository.count());
        model.addAttribute("nbInscriptions", inscriptionRepository.count());
        return "accueil";
    }
}
