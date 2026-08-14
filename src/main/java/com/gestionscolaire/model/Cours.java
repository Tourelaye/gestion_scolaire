package com.gestionscolaire.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cours")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le code du cours est obligatoire")
    @Column(unique = true, nullable = false, length = 20)
    private String code;

    @NotBlank(message = "L'intitulé est obligatoire")
    @Column(nullable = false, length = 150)
    private String intitule;

    @NotNull(message = "Le nombre de crédits est obligatoire")
    @Positive(message = "Les crédits doivent être positifs")
    @Column(nullable = false)
    private Integer credits;

    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inscription> inscriptions = new ArrayList<>();
}
