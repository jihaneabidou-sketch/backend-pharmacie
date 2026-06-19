package com.pharmacie.enligne.controller;

import com.pharmacie.enligne.entity.Ordonnance;
import com.pharmacie.enligne.service.OrdonnanceService;
import com.pharmacie.enligne.patterns.factory.NotificationFactory;
import com.pharmacie.enligne.patterns.factory.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pharmacist")
public class PharmacistController {

    private final OrdonnanceService ordonnanceService;
    private final NotificationFactory notificationFactory;

    public PharmacistController(OrdonnanceService ordonnanceService, NotificationFactory notificationFactory) {
        this.ordonnanceService = ordonnanceService;
        this.notificationFactory = notificationFactory;
    }

    // Endpoint 1 : Consulter la file d'attente des ordonnances reçues
    @GetMapping("/pharmacies/{pharmacyId}/file-attente")
    public ResponseEntity<List<Ordonnance>> obtenirFileAttente(@PathVariable Long pharmacyId) {
        List<Ordonnance> fileAttente = ordonnanceService.obtenirFileAttentePharmacie(pharmacyId);
        return ResponseEntity.ok(fileAttente);
    }

    // Endpoint 2 : Traiter l'ordonnance (Changement de statut, prix et notification automatique)
    @PutMapping("/ordonnances/{id}/traiter")
    public ResponseEntity<Ordonnance> traiterOrdonnance(
            @PathVariable Long id,
            @RequestParam("statut") String statut, // Ex: EN_PREPARATION, PRETE
            @RequestParam("montantDeBase") double montantDeBase,
            @RequestParam("typeAssurance") String typeAssurance) { // Ex: STANDARD ou ASSURANCE
        
        // 1. Mise à jour en BDD et exécution automatique de la Strategy de prix
        Ordonnance ordonnanceTraitee = ordonnanceService.traiterOrdonnance(id, statut, montantDeBase, typeAssurance);
        
        // 2. Déclenchement automatique de la Notification Usine (Factory Method) de façon Asynchrone
        NotificationService notification = notificationFactory.getNotificationChannel("EMAIL");
        String message = "Votre commande est passée au statut : " + statut + ". Montant final restant à votre charge : " + ordonnanceTraitee.getTotalAmount() + " MAD.";
        notification.envoyer(ordonnanceTraitee.getPatient().getEmail(), message);
        
        return ResponseEntity.ok(ordonnanceTraitee);
    }
}