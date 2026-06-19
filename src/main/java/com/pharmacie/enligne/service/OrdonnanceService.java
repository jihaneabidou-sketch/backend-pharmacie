package com.pharmacie.enligne.service;

import com.pharmacie.enligne.entity.Ordonnance;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface OrdonnanceService {
    Ordonnance soumettreOrdonnance(Long patientId, Long pharmacyId, MultipartFile file);
    Ordonnance traiterOrdonnance(Long ordonnanceId, String statut, double montantDeBase, String typeAssurance);
    List<Ordonnance> obtenirHistoriquePatient(Long patientId);
    List<Ordonnance> obtenirFileAttentePharmacie(Long pharmacyId);
}