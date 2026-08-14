package com.gestionscolaire.controller;

import com.gestionscolaire.model.Cours;
import com.gestionscolaire.model.Etudiant;
import com.gestionscolaire.service.CoursService;
import com.gestionscolaire.service.EtudiantService;
import com.gestionscolaire.service.InscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inscriptions")
@RequiredArgsConstructor
public class InscriptionController {

    private final InscriptionService inscriptionService;
    private final EtudiantService etudiantService;
    private final CoursService coursService;

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("inscriptions", inscriptionService.findAll());
        return "inscriptions/liste";
    }

    @GetMapping("/nouvelle")
    public String formulaireCreation(Model model) {
        model.addAttribute("etudiants", etudiantService.findAll());
        model.addAttribute("coursListe", coursService.findAll());
        return "inscriptions/formulaire";
    }

    @PostMapping("/enregistrer")
    public String enregistrer(@RequestParam Long etudiantId,
                               @RequestParam Long coursId,
                               RedirectAttributes redirectAttributes) {
        Etudiant etudiant = etudiantService.findById(etudiantId);
        Cours cours = coursService.findById(coursId);
        try {
            inscriptionService.inscrire(etudiant, cours);
            redirectAttributes.addFlashAttribute("succes", "Inscription enregistrée");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/inscriptions";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        inscriptionService.deleteById(id);
        redirectAttributes.addFlashAttribute("succes", "Inscription supprimée");
        return "redirect:/inscriptions";
    }
}
