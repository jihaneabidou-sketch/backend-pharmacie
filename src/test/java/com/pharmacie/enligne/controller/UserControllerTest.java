package com.pharmacie.enligne.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testObtenirUtilisateur_Succes() throws Exception {
        // On appelle l'API directement. Le contrôleur s'occupe de fabriquer et renvoyer le User.
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Vérifie le statut 200 OK
                .andExpect(jsonPath("$.id").value(1)); // Vérifie que l'ID renvoyé dans le JSON vaut bien 1
    }
}