package com.gestionscolaire.service;

import com.gestionscolaire.model.Cours;
import com.gestionscolaire.model.Etudiant;
import com.gestionscolaire.model.Inscription;
import com.gestionscolaire.repository.InscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InscriptionService {

    private final InscriptionRepository inscriptionRepository;

    public List<Inscription> findAll() {
        return inscriptionRepository.findAll();
    }

    public Inscription findById(Long id) {
        return inscriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inscription introuvable, id=" + id));
    }

    public Inscription inscrire(Etudiant etudiant, Cours cours) {
        if (inscriptionRepository.existsByEtudiantIdAndCoursId(etudiant.getId(), cours.getId())) {
            throw new IllegalStateException("Cet étudiant est déjà inscrit à ce cours");
        }
        Inscription inscription = new Inscription();
        inscription.setEtudiant(etudiant);
        inscription.setCours(cours);
        inscription.setDateInscription(LocalDate.now());
        return inscriptionRepository.save(inscription);
    }

    public void deleteById(Long id) {
        inscriptionRepository.deleteById(id);
    }

    public List<Inscription> findByEtudiantId(Long etudiantId) {
        return inscriptionRepository.findByEtudiantId(etudiantId);
    }

    public List<Inscription> findByCoursId(Long coursId) {
        return inscriptionRepository.findByCoursId(coursId);
    }
}
