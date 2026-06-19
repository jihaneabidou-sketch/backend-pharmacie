package com.pharmacie.enligne.controller;

import com.pharmacie.enligne.entity.Ordonnance;
import com.pharmacie.enligne.service.OrdonnanceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebMvcTest(OrdonnanceController.class)
public class OrdonnanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

  @MockitoBean
    private OrdonnanceService ordonnanceService;

    // =========================================================================
    // 1. Tester l'historique du patient (GET)
    // =========================================================================
    @Test
    public void testObtenirHistoriquePatient_Succes() throws Exception {
        Ordonnance o1 = new Ordonnance(); 
        o1.setId(10L); 
        o1.setStatus("RECOUE");
        
        Mockito.when(ordonnanceService.obtenirHistoriquePatient(1L))
               .thenReturn(Arrays.asList(o1));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/ordonnances/patient/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].status").value("RECOUE"));
    }

    // =========================================================================
    // 2. Tester l'envoi d'une ordonnance (POST Multipart / Form-Data)
    // =========================================================================
    @Test
    public void testSoumettreOrdonnance_Succes() throws Exception {
        MockMultipartFile fakeFile = new MockMultipartFile(
            "file", "ordonnance.txt", MediaType.TEXT_PLAIN_VALUE, "Contenu".getBytes()
        );

        Ordonnance oSaved = new Ordonnance(); 
        oSaved.setId(100L); 
        oSaved.setStatus("RECOUE");
        
        Mockito.when(ordonnanceService.soumettreOrdonnance(eq(1L), eq(2L), any()))
               .thenReturn(oSaved);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/ordonnances/soumettre")
                .file(fakeFile)
                .param("patientId", "1")
                .param("pharmacyId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.status").value("RECOUE"));
    }

    // =========================================================================
    // 3. Tester la file d'attente de la pharmacie (GET)
    // =========================================================================
    @Test
    public void testObtenirFileAttentePharmacie_Succes() throws Exception {
        Ordonnance o = new Ordonnance(); 
        o.setId(20L); 
        o.setStatus("RECOUE");
        
        Mockito.when(ordonnanceService.obtenirFileAttentePharmacie(2L))
               .thenReturn(Arrays.asList(o));

        // Note : Si votre URL réelle dans OrdonnanceController est différente 
        // (ex: /file-attente/{pharmacyId}), modifiez juste la ligne ci-dessous :
        mockMvc.perform(MockMvcRequestBuilders.get("/api/ordonnances/pharmacie/2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(20))
                .andExpect(jsonPath("$[0].status").value("RECOUE"));
    }
}