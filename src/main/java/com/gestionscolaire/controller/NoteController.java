package com.gestionscolaire.controller;

import com.gestionscolaire.model.Inscription;
import com.gestionscolaire.model.Note;
import com.gestionscolaire.service.InscriptionService;
import com.gestionscolaire.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    private final InscriptionService inscriptionService;

    /** Affiche les notes d'une inscription précise + sa moyenne */
    @GetMapping("/inscription/{inscriptionId}")
    public String parInscription(@PathVariable Long inscriptionId, Model model) {
        Inscription inscription = inscriptionService.findById(inscriptionId);
        model.addAttribute("inscription", inscription);
        model.addAttribute("notes", noteService.findByInscriptionId(inscriptionId));
        model.addAttribute("nouvelleNote", new Note());
        return "notes/liste";
    }

    @PostMapping("/ajouter/{inscriptionId}")
    public String ajouter(@PathVariable Long inscriptionId,
                           @Valid @ModelAttribute("nouvelleNote") Note note,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        Inscription inscription = inscriptionService.findById(inscriptionId);

        if (result.hasErrors()) {
            model.addAttribute("inscription", inscription);
            model.addAttribute("notes", noteService.findByInscriptionId(inscriptionId));
            return "notes/liste";
        }

        note.setInscription(inscription);
        noteService.save(note);
        redirectAttributes.addFlashAttribute("succes", "Note ajoutée");
        return "redirect:/notes/inscription/" + inscriptionId;
    }

    @GetMapping("/supprimer/{id}/{inscriptionId}")
    public String supprimer(@PathVariable Long id, @PathVariable Long inscriptionId,
                             RedirectAttributes redirectAttributes) {
        noteService.deleteById(id);
        redirectAttributes.addFlashAttribute("succes", "Note supprimée");
        return "redirect:/notes/inscription/" + inscriptionId;
    }
}
