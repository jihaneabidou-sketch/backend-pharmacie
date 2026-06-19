package com.pharmacie.enligne.patterns.strategy;

public class AssurancePricing implements PricingStrategy {
    @Override
    public double calculatePrice(double baseAmount) {
        return baseAmount * 0.30; 
    }
}