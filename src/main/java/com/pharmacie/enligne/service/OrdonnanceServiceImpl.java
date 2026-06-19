package com.pharmacie.enligne.service;

import com.pharmacie.enligne.entity.Ordonnance;
import com.pharmacie.enligne.entity.Pharmacy;
import com.pharmacie.enligne.entity.User;
import com.pharmacie.enligne.repository.OrdonnanceRepository;
import com.pharmacie.enligne.repository.PharmacyRepository;
import com.pharmacie.enligne.repository.UserRepository;
import com.pharmacie.enligne.patterns.strategy.*;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class OrdonnanceServiceImpl implements OrdonnanceService {

    private final OrdonnanceRepository ordonnanceRepository;
    private final UserRepository userRepository;
    private final PharmacyRepository pharmacyRepository;
    
    private final Path rootLocation = Paths.get("upload-dir");

    public OrdonnanceServiceImpl(OrdonnanceRepository ordonnanceRepository, 
                                 UserRepository userRepository, 
                                 PharmacyRepository pharmacyRepository) {
        this.ordonnanceRepository = ordonnanceRepository;
        this.userRepository = userRepository;
        this.pharmacyRepository = pharmacyRepository;
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Impossible d'initialiser le stockage des fichiers", e);
        }
    }

    @Override
    public Ordonnance soumettreOrdonnance(Long patientId, Long pharmacyId, MultipartFile file) {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient non trouvé"));
        Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new RuntimeException("Pharmacie non trouvée"));

        String originalName = file.getOriginalFilename();
        String extension = (originalName != null && originalName.contains(".")) 
                ? originalName.substring(originalName.lastIndexOf(".")) 
                : ".jpg";
        String nomHache = UUID.randomUUID().toString() + extension;

        try {
            Files.copy(file.getInputStream(), this.rootLocation.resolve(nomHache), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException("Échec du stockage du fichier", e);
        }

        Ordonnance ordonnance = Ordonnance.builder()
                .patient(patient)
                .pharmacy(pharmacy)
                .filePath(nomHache)
                .status("RECOUE")
                .build();

        return ordonnanceRepository.save(ordonnance);
    }

    @Override
    public Ordonnance traiterOrdonnance(Long ordonnanceId, String statut, double montantDeBase, String typeAssurance) {
        Ordonnance ordonnance = ordonnanceRepository.findById(ordonnanceId)
                .orElseThrow(() -> new RuntimeException("Ordonnance non trouvée"));

        PricingStrategy strategy;
        if ("ASSURANCE".equalsIgnoreCase(typeAssurance)) {
            strategy = new AssurancePricing();
        } else {
            strategy = new StandardPricing();
        }

        double montantFinal = strategy.calculatePrice(montantDeBase);
        ordonnance.setTotalAmount(montantFinal);
        ordonnance.setStatus(statut); 

        return ordonnanceRepository.save(ordonnance);
    }

    @Override
    public List<Ordonnance> obtenirHistoriquePatient(Long patientId) {
        return ordonnanceRepository.findByPatientId(patientId);
    }

    @Override
    public List<Ordonnance> obtenirFileAttentePharmacie(Long pharmacyId) {
        return ordonnanceRepository.findByPharmacyIdAndStatus(pharmacyId, "RECOUE");
    }
}