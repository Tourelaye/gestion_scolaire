package com.gestionscolaire.config;

import com.gestionscolaire.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration de la sécurité.
 * Deux rôles :
 *  - ADMIN       : accès complet (CRUD étudiants, cours, inscriptions, notes, suppression)
 *  - ENSEIGNANT  : peut consulter et saisir les notes, consulter étudiants/cours,
 *                  mais ne peut pas supprimer d'étudiants/cours ni gérer les inscriptions
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/webjars/**", "/login").permitAll()
                // Suppression réservée à l'ADMIN
                .requestMatchers("/etudiants/supprimer/**", "/cours/supprimer/**", "/inscriptions/supprimer/**").hasRole("ADMIN")
                .requestMatchers("/etudiants/nouveau/**", "/etudiants/modifier/**").hasRole("ADMIN")
                .requestMatchers("/cours/nouveau/**", "/cours/modifier/**").hasRole("ADMIN")
                .requestMatchers("/inscriptions/**").hasAnyRole("ADMIN", "ENSEIGNANT")
                .requestMatchers("/notes/**").hasAnyRole("ADMIN", "ENSEIGNANT")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            );

        return http.build();
    }
}
