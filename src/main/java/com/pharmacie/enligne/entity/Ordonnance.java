package com.pharmacie.enligne.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordonnances")
public class Ordonnance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacy_id", nullable = false)
    private Pharmacy pharmacy;

    @Column(name = "file_path", nullable = false)
    private String filePath; 

    @Column(nullable = false)
    private String status; 

    @Column(name = "total_amount")
    private Double totalAmount; 

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Constructeurs
    public Ordonnance() {}

    public Ordonnance(Long id, User patient, Pharmacy pharmacy, String filePath, String status, Double totalAmount, LocalDateTime createdAt) {
        this.id = id;
        this.patient = patient;
        this.pharmacy = pharmacy;
        this.filePath = filePath;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "RECOUE"; 
        }
    }

    // Getters et Setters manuels
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getPatient() { return patient; }
    public void setPatient(User patient) { this.patient = patient; }

    public Pharmacy getPharmacy() { return pharmacy; }
    public void setPharmacy(Pharmacy pharmacy) { this.pharmacy = pharmacy; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Implémentation manuelle du Pattern Builder pour OrdonnanceServiceImpl
    public static OrdonnanceBuilder builder() {
        return new OrdonnanceBuilder();
    }

    public static class OrdonnanceBuilder {
        private User patient;
        private Pharmacy pharmacy;
        private String filePath;
        private String status;

        public OrdonnanceBuilder patient(User patient) { this.patient = patient; return this; }
        public OrdonnanceBuilder pharmacy(Pharmacy pharmacy) { this.pharmacy = pharmacy; return this; }
        public OrdonnanceBuilder filePath(String filePath) { this.filePath = filePath; return this; }
        public OrdonnanceBuilder status(String status) { this.status = status; return this; }

        public Ordonnance build() {
            Ordonnance o = new Ordonnance();
            o.setPatient(this.patient);
            o.setPharmacy(this.pharmacy);
            o.setFilePath(this.filePath);
            o.setStatus(this.status);
            return o;
        }
    }
}