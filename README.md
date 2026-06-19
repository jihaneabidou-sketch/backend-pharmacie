# Système de Gestion pour Pharmacie en Ligne - Backend API

Ce dépôt centralise l'infrastructure backend d'une plateforme de gestion pharmaceutique en ligne. L'application prend en charge l'authentification des utilisateurs, la gestion des stocks officinaux et le traitement sécurisé des ordonnances médicales.

---

## Architecture et Technologies
* **Framework principal :** Java / Spring Boot
* **Persistance des données :** JPA / Hibernate
* **Système de gestion de base de données :** H2 (Environnement de développement) / MySQL
* **Framework de test :** JUnit 5 / Mockito

---

## Composants Applicatifs Majeurs
* **Pharmacy Management :** Contrôleurs dédiés à la manipulation des structures de données liées aux officines.
* **User Authentication :** Protocoles d'inscription, de connexion et de contrôle d'accès aux ressources.
* **Ordonnances Service :** Logique métier relative à la vérification et à la validation des prescriptions médicales.

---

## Validation et Suite de Tests
L'intégrité fonctionnelle de l'architecture est garantie par une suite de tests unitaires et d'intégration ciblant les couches critiques du système :
* Validation des interfaces d'entrée : `PharmacyControllerTest`, `UserControllerTest`
* Validation des patrons de conception : `NotificationFactoryTest`
* Validation des règles métiers : `OrdonnancesServiceTest`

Pour exécuter la suite de tests via l'outil de gestion de dépendances :
```bash
mvn test
