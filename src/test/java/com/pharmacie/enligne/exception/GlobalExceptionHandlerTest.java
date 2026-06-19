package com.pharmacie.enligne.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    @Test
    public void testHandleRuntimeException_RetourneErreurFormatee() {
        // GIVEN : On instancie le gestionnaire global et une fausse exception
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        RuntimeException fausseException = new RuntimeException("Ressource introuvable ou erreur serveur");

        // WHEN : On appelle manuellement la méthode d'interception
        ResponseEntity<Object> reponse = exceptionHandler.handleRuntimeException(fausseException);

        // THEN : On vérifie que la réponse HTTP est correcte (404 NOT_FOUND)
        assertEquals(HttpStatus.NOT_FOUND, reponse.getStatusCode(), "Le statut HTTP doit être 404.");

        // On vérifie que le corps (Body) contient les bonnes informations
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) reponse.getBody();
        
        assertNotNull(body, "Le corps de la réponse ne doit pas être nul.");
        assertTrue(body.containsKey("timestamp"), "Le JSON doit contenir un timestamp.");
        assertEquals("Ressource introuvable ou erreur serveur", body.get("message"), "Le message d'erreur doit correspondre.");
    }
}