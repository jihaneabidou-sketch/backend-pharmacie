package com.pharmacie.enligne.patterns.strategy;

public class StandardPricing implements PricingStrategy {
    @Override
    public double calculatePrice(double baseAmount) {
        return baseAmount;
    }
}