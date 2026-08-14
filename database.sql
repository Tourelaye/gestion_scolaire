-- ============================================================
-- Script SQL - Gestion Scolaire (JEE / Spring Boot)
-- Compatible PostgreSQL 13+
-- ============================================================
--
-- ETAPE 1 (a executer separement, en tant que superuser, hors de ce script) :
--   CREATE DATABASE gestion_scolaire;
--
-- ETAPE 2 : connectez-vous a la base puis executez le reste de ce script :
--   psql -U postgres -d gestion_scolaire -f database.sql
-- ============================================================

-- Nettoyage (si on relance le script sur une base existante)
DROP TABLE IF EXISTS note CASCADE;
DROP TABLE IF EXISTS inscription CASCADE;
DROP TABLE IF EXISTS cours CASCADE;
DROP TABLE IF EXISTS etudiant CASCADE;
DROP TABLE IF EXISTS utilisateur CASCADE;

-- ------------------------------------------------------------
-- Table : utilisateur (authentification, 2 rôles)
-- ------------------------------------------------------------
CREATE TABLE utilisateur (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- ------------------------------------------------------------
-- Table : etudiant
-- ------------------------------------------------------------
CREATE TABLE etudiant (
    id BIGSERIAL PRIMARY KEY,
    matricule VARCHAR(20) NOT NULL UNIQUE,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    date_naissance DATE
);

-- ------------------------------------------------------------
-- Table : cours
-- ------------------------------------------------------------
CREATE TABLE cours (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    intitule VARCHAR(150) NOT NULL,
    credits INT NOT NULL
);

-- ------------------------------------------------------------
-- Table : inscription (relation Etudiant <-> Cours)
-- ------------------------------------------------------------
CREATE TABLE inscription (
    id BIGSERIAL PRIMARY KEY,
    date_inscription DATE NOT NULL,
    etudiant_id BIGINT NOT NULL,
    cours_id BIGINT NOT NULL,
    CONSTRAINT fk_inscription_etudiant FOREIGN KEY (etudiant_id) REFERENCES etudiant(id) ON DELETE CASCADE,
    CONSTRAINT fk_inscription_cours FOREIGN KEY (cours_id) REFERENCES cours(id) ON DELETE CASCADE,
    CONSTRAINT uk_inscription_etudiant_cours UNIQUE (etudiant_id, cours_id)
);

-- ------------------------------------------------------------
-- Table : note (rattachée à une inscription)
-- ------------------------------------------------------------
CREATE TABLE note (
    id BIGSERIAL PRIMARY KEY,
    type_evaluation VARCHAR(50) NOT NULL,
    valeur DOUBLE PRECISION NOT NULL,
    coefficient DOUBLE PRECISION NOT NULL,
    inscription_id BIGINT NOT NULL,
    CONSTRAINT fk_note_inscription FOREIGN KEY (inscription_id) REFERENCES inscription(id) ON DELETE CASCADE
);

-- ============================================================
-- DONNÉES DE TEST
-- ============================================================

-- Utilisateurs (mots de passe encodés en BCrypt)
-- admin / admin123   -> rôle ADMIN
-- prof  / prof123    -> rôle ENSEIGNANT
INSERT INTO utilisateur (username, password, role) VALUES
('admin', '$2b$10$y5oO6DgiSI8k9K4NCjfnnO9Rhx9.jZCeCT0AjPJhMyUa7z9dnl/IO', 'ADMIN'),
('prof',  '$2b$10$aWlH5IqgnX5.Q9WiL/XN3O8KjcIdR5Ie8SJdeUap7efH7MWCet.f.', 'ENSEIGNANT');

-- Étudiants
INSERT INTO etudiant (matricule, nom, prenom, email, date_naissance) VALUES
('ET2026001', 'Diop', 'Awa', 'awa.diop@example.com', '2001-03-14'),
('ET2026002', 'Fall', 'Moussa', 'moussa.fall@example.com', '2000-07-22'),
('ET2026003', 'Ndiaye', 'Fatou', 'fatou.ndiaye@example.com', '2002-01-05'),
('ET2026004', 'Sarr', 'Ibrahima', 'ibrahima.sarr@example.com', '2001-11-30'),
('ET2026005', 'Ba', 'Aissatou', 'aissatou.ba@example.com', '2000-09-17');

-- Cours
INSERT INTO cours (code, intitule, credits) VALUES
('INF301', 'Programmation Java Avancée', 6),
('INF302', 'Bases de Données Relationnelles', 5),
('INF303', 'Développement Web JEE', 6),
('INF304', 'Génie Logiciel', 4);

-- Inscriptions
INSERT INTO inscription (date_inscription, etudiant_id, cours_id) VALUES
('2026-01-10', 1, 1),
('2026-01-10', 1, 2),
('2026-01-11', 2, 1),
('2026-01-11', 2, 3),
('2026-01-12', 3, 2),
('2026-01-12', 3, 3),
('2026-01-13', 4, 1),
('2026-01-13', 4, 4),
('2026-01-14', 5, 3),
('2026-01-14', 5, 4);

-- Notes (rattachées aux inscriptions ci-dessus, id 1 à 10 dans l'ordre)
INSERT INTO note (type_evaluation, valeur, coefficient, inscription_id) VALUES
-- Awa Diop - INF301
('Contrôle', 14.5, 1, 1),
('Examen', 16.0, 2, 1),
-- Awa Diop - INF302
('Contrôle', 12.0, 1, 2),
('Examen', 13.5, 2, 2),
-- Moussa Fall - INF301
('Contrôle', 10.0, 1, 3),
('Examen', 11.5, 2, 3),
-- Moussa Fall - INF303
('TP', 15.0, 1, 4),
('Examen', 14.0, 2, 4),
-- Fatou Ndiaye - INF302
('Contrôle', 17.0, 1, 5),
('Examen', 18.0, 2, 5),
-- Fatou Ndiaye - INF303
('TP', 13.0, 1, 6),
-- Ibrahima Sarr - INF301
('Contrôle', 9.0, 1, 7),
('Examen', 10.5, 2, 7),
-- Ibrahima Sarr - INF304
('Contrôle', 12.5, 1, 8),
-- Aissatou Ba - INF303
('TP', 16.5, 1, 9),
('Examen', 15.5, 2, 9);
-- Aissatou Ba - INF304 : pas encore de note (pour démontrer le cas "Pas encore notée")

-- ============================================================
-- Remise à niveau des séquences (BIGSERIAL) après insertions manuelles
-- Indispensable en PostgreSQL, sinon la prochaine insertion via l'appli
-- (ex: nouvel étudiant) peut entrer en conflit avec ces id fixes.
-- ============================================================
SELECT setval(pg_get_serial_sequence('utilisateur', 'id'), (SELECT MAX(id) FROM utilisateur));
SELECT setval(pg_get_serial_sequence('etudiant', 'id'), (SELECT MAX(id) FROM etudiant));
SELECT setval(pg_get_serial_sequence('cours', 'id'), (SELECT MAX(id) FROM cours));
SELECT setval(pg_get_serial_sequence('inscription', 'id'), (SELECT MAX(id) FROM inscription));
SELECT setval(pg_get_serial_sequence('note', 'id'), (SELECT MAX(id) FROM note));
