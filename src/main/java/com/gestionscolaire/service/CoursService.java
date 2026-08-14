package com.gestionscolaire.service;

import com.gestionscolaire.model.Cours;
import com.gestionscolaire.repository.CoursRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoursService {

    private final CoursRepository coursRepository;

    public List<Cours> findAll() {
        return coursRepository.findAll();
    }

    public Cours findById(Long id) {
        return coursRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cours introuvable, id=" + id));
    }

    public Cours save(Cours cours) {
        return coursRepository.save(cours);
    }

    public void deleteById(Long id) {
        coursRepository.deleteById(id);
    }

    public boolean codeExiste(String code) {
        return coursRepository.existsByCode(code);
    }
}
