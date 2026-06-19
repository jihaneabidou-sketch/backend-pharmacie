package com.pharmacie.enligne.patterns.factory;

import org.junit.jupiter.api.Test;

public class EmailNotificationServiceImplTest {

    @Test
    public void testEnvoyerEmail_Succes() {
        NotificationService emailService = new EmailNotificationServiceImpl();
        // On exécute simplement la méthode pour valider la ligne de code et le Sysout
        emailService.envoyer("test@patient.com", "Votre ordonnance est prête !");
    }
}