package com.pharmacie.enligne.controller;

import com.pharmacie.enligne.entity.Ordonnance;
import com.pharmacie.enligne.service.OrdonnanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patient")
public class PatientController {

    private final OrdonnanceService ordonnanceService;

    public PatientController(OrdonnanceService ordonnanceService) {
        this.ordonnanceService = ordonnanceService;
    }

    // Endpoint 1 : Téléverser une ordonnance (Format MultipartFile pour Image/PDF)
    @PostMapping("/ordonnances/upload")
    public ResponseEntity<Ordonnance> uploaderOrdonnance(
            @RequestParam("patientId") Long patientId,
            @RequestParam("pharmacyId") Long pharmacyId,
            @RequestParam("file") MultipartFile file) {
        
        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Ordonnance nouvelleOrdonnance = ordonnanceService.soumettreOrdonnance(patientId, pharmacyId, file);
        return new ResponseEntity<>(nouvelleOrdonnance, HttpStatus.CREATED); // Code 201 Created
    }

    // Endpoint 2 : Consulter l'historique de ses ordonnances
    @GetMapping("/ordonnances/historique/{patientId}")
    public ResponseEntity<List<Ordonnance>> obtenirHistorique(@PathVariable Long patientId) {
        List<Ordonnance> historique = ordonnanceService.obtenirHistoriquePatient(patientId);
        return ResponseEntity.ok(historique); // Code 200 OK
    }
}