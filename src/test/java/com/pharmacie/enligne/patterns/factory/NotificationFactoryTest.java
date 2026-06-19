package com.pharmacie.enligne.patterns.factory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class NotificationFactoryTest {

    @Mock
    private ApplicationContext context; // On simule le contexte Spring

    @Mock
    private NotificationService emailNotificationMock; // On simule le service d'email

    @InjectMocks
    private NotificationFactory notificationFactory; // On injecte le faux contexte dans la factory

    // =========================================================================
    // Scénario 1 : Demande d'un canal EMAIL (Cas de succès)
    // =========================================================================
    @Test
    public void testGetNotificationChannel_Email_Succes() {
        // GIVEN : Quand la factory demande "emailNotification" au contexte, il renvoie notre mock
        Mockito.when(context.getBean("emailNotification")).thenReturn(emailNotificationMock);

        // WHEN : On appelle la factory avec "EMAIL" (teste aussi l'insensibilité à la casse)
        NotificationService resultat = notificationFactory.getNotificationChannel("email");

        // THEN : On vérifie qu'on récupère bien le bon service
        assertNotNull(resultat, "Le canal de notification ne doit pas être nul.");
        assertEquals(emailNotificationMock, resultat, "La factory doit retourner le bean d'email.");
    }

    // =========================================================================
    // Scénario 2 : Demande d'un canal inconnu (Couverture de l'exception)
    // =========================================================================
    @Test
    public void testGetNotificationChannel_TypeInconnu_LeveException() {
        // WHEN & THEN : On s'attend à ce qu'une IllegalArgumentException soit levée
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            notificationFactory.getNotificationChannel("SMS"); // Type non géré pour l'instant
        });

        assertEquals("Type de notification inconnu", exception.getMessage());
    }
}