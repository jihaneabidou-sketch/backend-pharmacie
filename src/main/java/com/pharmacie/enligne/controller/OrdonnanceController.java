package com.pharmacie.enligne.controller;

import com.pharmacie.enligne.entity.Ordonnance;
import com.pharmacie.enligne.service.OrdonnanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/ordonnances")
public class OrdonnanceController {

    private final OrdonnanceService ordonnanceService;

    // Injection du service via le constructeur
    public OrdonnanceController(OrdonnanceService ordonnanceService) {
        this.ordonnanceService = ordonnanceService;
    }
@GetMapping("/pharmacie/{pharmacyId}")
public ResponseEntity<List<Ordonnance>> obtenirFileAttentePharmacie(@PathVariable Long pharmacyId) {
    List<Ordonnance> fileAttente = ordonnanceService.obtenirFileAttentePharmacie(pharmacyId);
    return ResponseEntity.ok(fileAttente);
}
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Ordonnance>> obtenirHistoriquePatient(@PathVariable Long patientId) {
        List<Ordonnance> historique = ordonnanceService.obtenirHistoriquePatient(patientId);
        return ResponseEntity.ok(historique);
    }

    @PostMapping("/soumettre")
    public ResponseEntity<Ordonnance> soumettreOrdonnance(
            @RequestParam("patientId") Long patientId,
            @RequestParam("pharmacyId") Long pharmacyId,
            @RequestParam("file") MultipartFile file) {
        Ordonnance ordonnance = ordonnanceService.soumettreOrdonnance(patientId, pharmacyId, file);
        return ResponseEntity.ok(ordonnance);
    }
}