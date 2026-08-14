package com.gestionscolaire.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inscription", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"etudiant_id", "cours_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_inscription", nullable = false)
    private LocalDate dateInscription = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cours_id", nullable = false)
    private Cours cours;

    @OneToMany(mappedBy = "inscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> notes = new ArrayList<>();

    /**
     * Calcule la moyenne pondérée des notes de cette inscription
     * (somme(valeur * coefficient) / somme(coefficient))
     */
    @Transient
    public Double getMoyenne() {
        if (notes == null || notes.isEmpty()) {
            return null;
        }
        double sommePonderee = 0;
        double sommeCoeff = 0;
        for (Note n : notes) {
            sommePonderee += n.getValeur() * n.getCoefficient();
            sommeCoeff += n.getCoefficient();
        }
        return sommeCoeff == 0 ? null : sommePonderee / sommeCoeff;
    }
}
