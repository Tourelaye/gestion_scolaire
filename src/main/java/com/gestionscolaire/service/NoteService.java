package com.gestionscolaire.service;

import com.gestionscolaire.model.Inscription;
import com.gestionscolaire.model.Note;
import com.gestionscolaire.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public List<Note> findByInscriptionId(Long inscriptionId) {
        return noteRepository.findByInscriptionId(inscriptionId);
    }

    public Note save(Note note) {
        return noteRepository.save(note);
    }

    public Note findById(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Note introuvable, id=" + id));
    }

    public void deleteById(Long id) {
        noteRepository.deleteById(id);
    }

    /**
     * Calcule la moyenne pondérée d'une inscription (utile côté service aussi,
     * par ex. pour un futur relevé de notes global).
     */
    public Double calculerMoyenne(Inscription inscription) {
        return inscription.getMoyenne();
    }
}
