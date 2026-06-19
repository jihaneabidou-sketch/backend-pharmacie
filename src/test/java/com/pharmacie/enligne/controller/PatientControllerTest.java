package com.pharmacie.enligne.controller;

import com.pharmacie.enligne.entity.Ordonnance;
import com.pharmacie.enligne.service.OrdonnanceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrdonnanceService ordonnanceService;

    @Test
    public void testUploaderOrdonnance_Succes() throws Exception {
        MockMultipartFile fakeFile = new MockMultipartFile(
            "file", "photo.jpg", MediaType.IMAGE_JPEG_VALUE, "fake-image-content".getBytes()
        );

        Ordonnance o = new Ordonnance();
        o.setId(50L);

        Mockito.when(ordonnanceService.soumettreOrdonnance(eq(1L), eq(2L), any())).thenReturn(o);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/patient/ordonnances/upload")
                .file(fakeFile)
                .param("patientId", "1")
                .param("pharmacyId", "2"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(50));
    }

    @Test
    public void testUploaderOrdonnance_Echec_FichierVide() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile(
            "file", "vide.txt", MediaType.TEXT_PLAIN_VALUE, new byte[0]
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/patient/ordonnances/upload")
                .file(emptyFile)
                .param("patientId", "1")
                .param("pharmacyId", "2"))
                .andExpect(status().isBadRequest()); // Couvre la branche d'erreur "file.isEmpty()"
    }

    @Test
    public void testObtenirHistorique_Succes() throws Exception {
        Ordonnance o = new Ordonnance();
        o.setId(60L);

        Mockito.when(ordonnanceService.obtenirHistoriquePatient(1L)).thenReturn(Arrays.asList(o));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/patient/ordonnances/historique/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(60));
    }
}