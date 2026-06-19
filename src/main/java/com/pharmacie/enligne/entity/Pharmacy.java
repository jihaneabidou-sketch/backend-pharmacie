package com.pharmacie.enligne.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pharmacies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pharmacy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;
    
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true; // Géré par l'admin (active / inactive) [cite: 28]
}