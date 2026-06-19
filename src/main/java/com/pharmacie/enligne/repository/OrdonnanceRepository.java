package com.pharmacie.enligne.repository;

import com.pharmacie.enligne.entity.Ordonnance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdonnanceRepository extends JpaRepository<Ordonnance, Long> {
    // Pour l'historique du patient
    List<Ordonnance> findByPatientId(Long patientId);
    
    // Pour la file d'attente du pharmacien
    List<Ordonnance> findByPharmacyIdAndStatus(Long pharmacyId, String status);
}