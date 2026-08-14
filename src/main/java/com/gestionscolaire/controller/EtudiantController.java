package com.gestionscolaire.controller;

import com.gestionscolaire.model.Etudiant;
import com.gestionscolaire.service.EtudiantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/etudiants")
@RequiredArgsConstructor
public class EtudiantController {

    private final EtudiantService etudiantService;

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("etudiants", etudiantService.findAll());
        return "etudiants/liste";
    }

    @GetMapping("/nouveau")
    public String formulaireCreation(Model model) {
        model.addAttribute("etudiant", new Etudiant());
        return "etudiants/formulaire";
    }

    @GetMapping("/modifier/{id}")
    public String formulaireModification(@PathVariable Long id, Model model) {
        model.addAttribute("etudiant", etudiantService.findById(id));
        return "etudiants/formulaire";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("etudiant", etudiantService.findById(id));
        return "etudiants/detail";
    }

    @PostMapping("/enregistrer")
    public String enregistrer(@Valid @ModelAttribute("etudiant") Etudiant etudiant,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "etudiants/formulaire";
        }

        // Vérifier l'unicité du matricule/email seulement à la création ou si modifié
        if (etudiant.getId() == null && etudiantService.matriculeExiste(etudiant.getMatricule())) {
            result.rejectValue("matricule", "error.etudiant", "Ce matricule existe déjà");
            return "etudiants/formulaire";
        }

        etudiantService.save(etudiant);
        redirectAttributes.addFlashAttribute("succes", "Étudiant enregistré avec succès");
        return "redirect:/etudiants";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        etudiantService.deleteById(id);
        redirectAttributes.addFlashAttribute("succes", "Étudiant supprimé");
        return "redirect:/etudiants";
    }
}
