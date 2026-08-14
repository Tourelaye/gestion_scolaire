package com.gestionscolaire.config;

import com.gestionscolaire.model.Role;
import com.gestionscolaire.model.Utilisateur;
import com.gestionscolaire.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Crée deux comptes par défaut au premier démarrage (utile pour la démo/soutenance) :
 *  - admin / admin123      (ROLE_ADMIN)
 *  - prof  / prof123       (ROLE_ENSEIGNANT)
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (utilisateurRepository.findByUsername("admin").isEmpty()) {
            Utilisateur admin = new Utilisateur();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            utilisateurRepository.save(admin);
        }

        if (utilisateurRepository.findByUsername("prof").isEmpty()) {
            Utilisateur prof = new Utilisateur();
            prof.setUsername("prof");
            prof.setPassword(passwordEncoder.encode("prof123"));
            prof.setRole(Role.ENSEIGNANT);
            utilisateurRepository.save(prof);
        }
    }
}
