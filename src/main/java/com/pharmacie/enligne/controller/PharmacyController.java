package com.pharmacie.enligne.controller;

import com.pharmacie.enligne.entity.Pharmacy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pharmacies")
public class PharmacyController {

    @GetMapping
    public ResponseEntity<List<Pharmacy>> listerToutesLesPharmacies() {
        List<Pharmacy> pharmacies = new ArrayList<>();
        return ResponseEntity.ok(pharmacies);
    }
}