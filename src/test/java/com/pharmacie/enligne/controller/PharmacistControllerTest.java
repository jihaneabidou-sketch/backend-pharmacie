package com.pharmacie.enligne.controller;

import com.pharmacie.enligne.entity.Ordonnance;
import com.pharmacie.enligne.entity.User;
import com.pharmacie.enligne.service.OrdonnanceService;
import com.pharmacie.enligne.patterns.factory.NotificationFactory;
import com.pharmacie.enligne.patterns.factory.NotificationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq; // <-- L'import manquant est ici !

@WebMvcTest(PharmacistController.class)
public class PharmacistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrdonnanceService ordonnanceService;

    @MockitoBean
    private NotificationFactory notificationFactory;

    @MockitoBean
    private NotificationService notificationService;

    @Test
    public void testObtenirFileAttente_Succes() throws Exception {
        Ordonnance o = new Ordonnance();
        o.setId(70L);

        Mockito.when(ordonnanceService.obtenirFileAttentePharmacie(1L)).thenReturn(Arrays.asList(o));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/pharmacist/pharmacies/1/file-attente")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(70));
    }

    @Test
    public void testTraiterOrdonnance_Succes() throws Exception {
        // Préparation du faux patient attaché à l'ordonnance pour éviter un NullPointerException sur getEmail()
        User mockPatient = new User();
        mockPatient.setEmail("patient@test.com");

        Ordonnance oTraitee = new Ordonnance();
        oTraitee.setId(80L);
        oTraitee.setTotalAmount(150.0);
        oTraitee.setPatient(mockPatient);

        // Mocks de la couche Service et de la Factory de notifications
        Mockito.when(ordonnanceService.traiterOrdonnance(80L, "PRETE", 100.0, "STANDARD")).thenReturn(oTraitee);
        Mockito.when(notificationFactory.getNotificationChannel("EMAIL")).thenReturn(notificationService);

        // Déclenchement de la requête PUT avec tous les paramètres requis
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/pharmacist/ordonnances/80/traiter")
                .param("statut", "PRETE")
                .param("montantDeBase", "100.0")
                .param("typeAssurance", "STANDARD")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(80))
                .andExpect(jsonPath("$.totalAmount").value(150.0));

        // Vérification que la notification a bien été déclenchée
        Mockito.verify(notificationService, Mockito.times(1)).envoyer(eq("patient@test.com"), anyString());
    }
}