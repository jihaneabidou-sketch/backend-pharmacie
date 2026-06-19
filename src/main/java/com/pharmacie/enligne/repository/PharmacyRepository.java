package com.pharmacie.enligne.repository;

import com.pharmacie.enligne.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
}