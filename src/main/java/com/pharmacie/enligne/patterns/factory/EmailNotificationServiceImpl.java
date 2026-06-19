package com.pharmacie.enligne.patterns.factory;

import org.springframework.stereotype.Service;

@Service("emailNotification")
public class EmailNotificationServiceImpl implements NotificationService {
    @Override
    public void envoyer(String destinataire, String message) {
        // Simulation d'envoi asynchrone (Mock demandé par le cahier des charges)
        System.out.println("[ASYNC - EMAIL] Envoyé à " + destinataire + " : " + message);
    }
}