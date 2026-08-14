package com.gestionscolaire.repository;

import com.gestionscolaire.model.Cours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoursRepository extends JpaRepository<Cours, Long> {
    Optional<Cours> findByCode(String code);
    boolean existsByCode(String code);
}
