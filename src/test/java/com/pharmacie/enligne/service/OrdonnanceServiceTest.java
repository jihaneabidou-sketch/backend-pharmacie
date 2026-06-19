package com.pharmacie.enligne.service;

import com.pharmacie.enligne.entity.Ordonnance;
import com.pharmacie.enligne.entity.Pharmacy;
import com.pharmacie.enligne.entity.User;
import com.pharmacie.enligne.repository.OrdonnanceRepository;
import com.pharmacie.enligne.repository.PharmacyRepository;
import com.pharmacie.enligne.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class OrdonnanceServiceTest {

    @Mock
    private OrdonnanceRepository ordonnanceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PharmacyRepository pharmacyRepository;

    @InjectMocks
    private OrdonnanceServiceImpl ordonnanceService;

    // =========================================================================
    // TESTS : traiterOrdonnance
    // =========================================================================

    @Test
    public void testTraiterOrdonnance_AvecAssurance_AppliqueReduction() {
        Ordonnance ordonnanceMock = new Ordonnance();
        ordonnanceMock.setId(1L);
        ordonnanceMock.setStatus("RECOUE");

        Mockito.when(ordonnanceRepository.findById(1L)).thenReturn(Optional.of(ordonnanceMock));
        Mockito.when(ordonnanceRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        Ordonnance resultat = ordonnanceService.traiterOrdonnance(1L, "EN_PREPARATION", 100.0, "ASSURANCE");

        assertEquals(30.0, resultat.getTotalAmount());
        assertEquals("EN_PREPARATION", resultat.getStatus());
    }

    @Test
    public void testTraiterOrdonnance_SansAssurance_PleinTarif() {
        Ordonnance ordonnanceMock = new Ordonnance();
        ordonnanceMock.setId(1L);
        ordonnanceMock.setStatus("RECOUE");

        Mockito.when(ordonnanceRepository.findById(1L)).thenReturn(Optional.of(ordonnanceMock));
        Mockito.when(ordonnanceRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        Ordonnance resultat = ordonnanceService.traiterOrdonnance(1L, "EN_PREPARATION", 100.0, "AUCUNE");

        assertEquals(100.0, resultat.getTotalAmount());
    }

    @Test
    public void testTraiterOrdonnance_IdIntrouvable_LeveException() {
        Mockito.when(ordonnanceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            ordonnanceService.traiterOrdonnance(99L, "EN_PREPARATION", 100.0, "ASSURANCE");
        });
    }

    // =========================================================================
    // TESTS : soumettreOrdonnance
    // =========================================================================

    @Test
    public void testSoumettreOrdonnance_Succes() {
        Long patientId = 1L;
        Long pharmacyId = 2L;
        
        User patient = new User();
        Pharmacy pharmacy = new Pharmacy();
        
        MockMultipartFile mockFile = new MockMultipartFile(
            "file", "ordonnance.pdf", "application/pdf", "Contenu fictif".getBytes()
        );

        Mockito.when(userRepository.findById(patientId)).thenReturn(Optional.of(patient));
        Mockito.when(pharmacyRepository.findById(pharmacyId)).thenReturn(Optional.of(pharmacy));
        Mockito.when(ordonnanceRepository.save(any(Ordonnance.class))).thenAnswer(i -> i.getArguments()[0]);

        Ordonnance resultat = ordonnanceService.soumettreOrdonnance(patientId, pharmacyId, mockFile);

        assertNotNull(resultat);
        assertEquals("RECOUE", resultat.getStatus());
        assertTrue(resultat.getFilePath().endsWith(".pdf"));
    }

    @Test
    public void testSoumettreOrdonnance_PatientIntrouvable_LeveException() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "data".getBytes());

        assertThrows(RuntimeException.class, () -> {
            ordonnanceService.soumettreOrdonnance(1L, 2L, mockFile);
        });
    }

    @Test
    public void testSoumettreOrdonnance_PharmacieIntrouvable_LeveException() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        Mockito.when(pharmacyRepository.findById(2L)).thenReturn(Optional.empty());
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "data".getBytes());

        assertThrows(RuntimeException.class, () -> {
            ordonnanceService.soumettreOrdonnance(1L, 2L, mockFile);
        });
    }

    @Test
    public void testSoumettreOrdonnance_EchecStockageFichier_LeveException() throws IOException {
        Long patientId = 1L;
        Long pharmacyId = 2L;

        Mockito.when(userRepository.findById(patientId)).thenReturn(Optional.of(new User()));
        Mockito.when(pharmacyRepository.findById(pharmacyId)).thenReturn(Optional.of(new Pharmacy()));

        MultipartFile fileMockMalicieux = Mockito.mock(MultipartFile.class);
        Mockito.when(fileMockMalicieux.getOriginalFilename()).thenReturn("photo.jpg");
        Mockito.when(fileMockMalicieux.getInputStream()).thenThrow(new IOException("Simulated disk error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            ordonnanceService.soumettreOrdonnance(patientId, pharmacyId, fileMockMalicieux);
        });

        assertTrue(exception.getMessage().contains("Échec du stockage du fichier"));
    }

    @Test
    public void testSoumettreOrdonnance_FichierSansExtension_UtiliseExtensionParDefaut() {
        Long patientId = 1L;
        Long pharmacyId = 2L;
        
        MockMultipartFile mockFileElementaire = new MockMultipartFile(
            "file", "filenameWithNoExtension", "image/jpeg", "data".getBytes()
        );

        Mockito.when(userRepository.findById(patientId)).thenReturn(Optional.of(new User()));
        Mockito.when(pharmacyRepository.findById(pharmacyId)).thenReturn(Optional.of(new Pharmacy()));
        Mockito.when(ordonnanceRepository.save(any(Ordonnance.class))).thenAnswer(i -> i.getArguments()[0]);

        Ordonnance resultat = ordonnanceService.soumettreOrdonnance(patientId, pharmacyId, mockFileElementaire);

        assertNotNull(resultat);
        assertTrue(resultat.getFilePath().endsWith(".jpg"));
    }

    @Test
    public void testSoumettreOrdonnance_NomDeFichierNul_UtiliseExtensionParDefaut() throws IOException {
        Long patientId = 1L;
        Long pharmacyId = 2L;
        
        MultipartFile mockFileNomNul = Mockito.mock(MultipartFile.class);
        Mockito.when(mockFileNomNul.getOriginalFilename()).thenReturn(null);
        Mockito.when(mockFileNomNul.getInputStream()).thenReturn(new java.io.ByteArrayInputStream("data".getBytes()));

        Mockito.when(userRepository.findById(patientId)).thenReturn(Optional.of(new User()));
        Mockito.when(pharmacyRepository.findById(pharmacyId)).thenReturn(Optional.of(new Pharmacy()));
        Mockito.when(ordonnanceRepository.save(any(Ordonnance.class))).thenAnswer(i -> i.getArguments()[0]);

        Ordonnance resultat = ordonnanceService.soumettreOrdonnance(patientId, pharmacyId, mockFileNomNul);

        assertNotNull(resultat);
        assertTrue(resultat.getFilePath().endsWith(".jpg"));
    }

    // =========================================================================
    // TESTS : Historique et File d'attente
    // =========================================================================

    @Test
    public void testObtenirHistoriquePatient() {
        List<Ordonnance> listeMock = Arrays.asList(new Ordonnance(), new Ordonnance());
        Mockito.when(ordonnanceRepository.findByPatientId(1L)).thenReturn(listeMock);

        List<Ordonnance> resultat = ordonnanceService.obtenirHistoriquePatient(1L);

        assertEquals(2, resultat.size());
    }

    @Test
    public void testObtenirFileAttentePharmacie() {
        List<Ordonnance> fileMock = Arrays.asList(new Ordonnance());
        Mockito.when(ordonnanceRepository.findByPharmacyIdAndStatus(2L, "RECOUE")).thenReturn(fileMock);

        List<Ordonnance> resultat = ordonnanceService.obtenirFileAttentePharmacie(2L);

        assertEquals(1, resultat.size());
    }
}