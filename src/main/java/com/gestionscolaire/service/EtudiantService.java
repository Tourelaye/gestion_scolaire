package com.gestionscolaire.service;

import com.gestionscolaire.model.Etudiant;
import com.gestionscolaire.repository.EtudiantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;

    public List<Etudiant> findAll() {
        return etudiantRepository.findAll();
    }

    public Etudiant findById(Long id) {
        return etudiantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Étudiant introuvable, id=" + id));
    }

    public Etudiant save(Etudiant etudiant) {
        return etudiantRepository.save(etudiant);
    }

    public void deleteById(Long id) {
        etudiantRepository.deleteById(id);
    }

    public boolean matriculeExiste(String matricule) {
        return etudiantRepository.existsByMatricule(matricule);
    }

    public boolean emailExiste(String email) {
        return etudiantRepository.existsByEmail(email);
    }
}
