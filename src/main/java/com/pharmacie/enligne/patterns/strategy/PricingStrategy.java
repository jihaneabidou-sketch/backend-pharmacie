package com.pharmacie.enligne.patterns.strategy;

public interface PricingStrategy {
    double calculatePrice(double baseAmount);
}