package com.gestionscolaire.controller;

import com.gestionscolaire.model.Cours;
import com.gestionscolaire.service.CoursService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cours")
@RequiredArgsConstructor
public class CoursController {

    private final CoursService coursService;

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("coursListe", coursService.findAll());
        return "cours/liste";
    }

    @GetMapping("/nouveau")
    public String formulaireCreation(Model model) {
        model.addAttribute("cours", new Cours());
        return "cours/formulaire";
    }

    @GetMapping("/modifier/{id}")
    public String formulaireModification(@PathVariable Long id, Model model) {
        model.addAttribute("cours", coursService.findById(id));
        return "cours/formulaire";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("cours", coursService.findById(id));
        return "cours/detail";
    }

    @PostMapping("/enregistrer")
    public String enregistrer(@Valid @ModelAttribute("cours") Cours cours,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "cours/formulaire";
        }

        if (cours.getId() == null && coursService.codeExiste(cours.getCode())) {
            result.rejectValue("code", "error.cours", "Ce code de cours existe déjà");
            return "cours/formulaire";
        }

        coursService.save(cours);
        redirectAttributes.addFlashAttribute("succes", "Cours enregistré avec succès");
        return "redirect:/cours";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        coursService.deleteById(id);
        redirectAttributes.addFlashAttribute("succes", "Cours supprimé");
        return "redirect:/cours";
    }
}
