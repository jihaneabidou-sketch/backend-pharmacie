package com.pharmacie.enligne.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PharmacyController.class)
public class PharmacyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testListerToutesLesPharmacies_Succes() throws Exception {
        // L'API est appelée, elle va créer l'ArrayList vide et la renvoyer
        mockMvc.perform(MockMvcRequestBuilders.get("/api/pharmacies")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Vérifie le statut 200 OK
                .andExpect(jsonPath("$").isArray()) // Vérifie que c'est un tableau JSON
                .andExpect(jsonPath("$.length()").value(0)); // Vérifie que le tableau contient 0 élément
    }
}