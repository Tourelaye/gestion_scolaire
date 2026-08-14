# Gestion Scolaire — Projet Final JEE

Application web de gestion scolaire (étudiants, cours, inscriptions, notes) développée avec
**Spring Boot**, **Spring MVC**, **Spring Data JPA / Hibernate**, **Thymeleaf + Bootstrap**
et **Spring Security**.

## Fonctionnalités
- Structure MVC en packages (`model`, `repository`, `service`, `controller`, `config`)
- Entités JPA avec relations : `Etudiant` (1-*) `Inscription` (*-1) `Cours`, `Inscription` (1-*) `Note`
- CRUD complet Étudiants (liste, ajout, modification, suppression)
- CRUD Cours + gestion des inscriptions (association étudiant/cours)
- Saisie et affichage des notes avec **calcul automatique de la moyenne pondérée**
  (`somme(valeur × coefficient) / somme(coefficient)`)
- Interface responsive avec Bootstrap 5
- Authentification par formulaire + 2 rôles (`ADMIN`, `ENSEIGNANT`) avec restrictions
  d'accès par URL (suppression réservée à l'ADMIN, saisie des notes ouverte aux deux rôles)

## Prérequis
- Java 17+
- Maven 3.8+
- PostgreSQL 13+ (un serveur PostgreSQL démarré localement)

## Installation

### 1. Base de données
Contrairement à MySQL, PostgreSQL ne crée pas la base automatiquement : il faut la créer
vous-même avant de lancer le script.

```bash
# Se connecter à PostgreSQL et créer la base
psql -U postgres -c "CREATE DATABASE gestion_scolaire;"

# Puis charger le schéma + les données de test
psql -U postgres -d gestion_scolaire -f database.sql
```

Le script `database.sql` crée les 5 tables (`utilisateur`, `etudiant`, `cours`,
`inscription`, `note`) et insère des données de test, y compris les comptes
admin/prof déjà encodés en BCrypt.

**Alternative** : si vous préférez laisser Hibernate créer les tables tout seul
(`spring.jpa.hibernate.ddl-auto=update`), il suffit de créer la base vide
(`CREATE DATABASE gestion_scolaire;`) sans lancer `database.sql`. Les comptes
admin/prof seront alors créés automatiquement au démarrage par `DataInitializer`,
mais vous n'aurez pas les étudiants/cours de démo.

### 2. Configuration
Modifier si besoin `src/main/resources/application.properties` :
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gestion_scolaire
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### 3. Lancer l'application
```bash
mvn spring-boot:run
```
L'application est accessible sur **http://localhost:8080**

## Comptes de démonstration
| Utilisateur | Mot de passe | Rôle       |
|-------------|---------------|-----------|
| admin       | admin123      | ADMIN     |
| prof        | prof123       | ENSEIGNANT|

## Structure du projet
```
src/main/java/com/gestionscolaire/
├── GestionScolaireApplication.java
├── config/          → SecurityConfig, DataInitializer
├── model/           → Etudiant, Cours, Inscription, Note, Utilisateur, Role
├── repository/      → interfaces Spring Data JPA
├── service/         → logique métier
└── controller/      → contrôleurs Spring MVC

src/main/resources/
├── templates/       → vues Thymeleaf (accueil, login, etudiants/, cours/, inscriptions/, notes/)
├── static/css/      → styles personnalisés
└── application.properties

database.sql         → script de création + données de test
```

## Points à personnaliser avant la soutenance
- Adapter le nom du package si besoin (actuellement `com.gestionscolaire`)
- Ajouter un diagramme UML des classes dans le rapport technique (diagramme de classes
  correspond directement aux entités JPA ci-dessus)
- Prendre des captures d'écran de chaque fonctionnalité pour le rapport PDF
