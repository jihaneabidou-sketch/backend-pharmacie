package com.pharmacie.enligne.patterns.factory;

import org.springframework.scheduling.annotation.Async;

public interface NotificationService {
    @Async
    void envoyer(String destinataire, String message);
}