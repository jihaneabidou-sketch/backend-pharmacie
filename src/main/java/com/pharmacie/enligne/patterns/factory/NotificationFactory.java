package com.pharmacie.enligne.patterns.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class NotificationFactory {

    @Autowired
    private ApplicationContext context;

    public NotificationService getNotificationChannel(String type) {
        if ("EMAIL".equalsIgnoreCase(type)) {
            return (NotificationService) context.getBean("emailNotification");
        }
        // Vous pourrez rajouter le SMS de la même façon ici
        throw new IllegalArgumentException("Type de notification inconnu");
    }
}