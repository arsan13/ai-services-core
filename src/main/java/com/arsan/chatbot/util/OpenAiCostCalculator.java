package com.arsan.chatbot.util;

import java.util.Map;

public class OpenAiCostCalculator {

    // USD per 1K tokens
    private static final Map<String, ModelPricing> PRICING = Map.of(
            "gpt-4o", new ModelPricing(0.005, 0.015),
            "gpt-4o-mini", new ModelPricing(0.00015, 0.0006),
            "gpt-4-turbo", new ModelPricing(0.01, 0.03),
            "gpt-3.5-turbo", new ModelPricing(0.0005, 0.0015)
    );

    public static double calculateCost(String model, int promptTokens, int completionTokens) {

        ModelPricing pricing = PRICING.getOrDefault(normalize(model), defaultPricing());

        double inputCost = (promptTokens / 1000.0) * pricing.inputCostPer1K;
        double outputCost = (completionTokens / 1000.0) * pricing.outputCostPer1K;

        return round(inputCost + outputCost);
    }

    private static String normalize(String model) {
        return model != null ? model.toLowerCase().trim() : "";
    }

    private static ModelPricing defaultPricing() {
        return new ModelPricing(0.0005, 0.0015);
    }

    private static double round(double value) {
        return Math.round(value * 1_000_000d) / 1_000_000d;
    }

    private record ModelPricing(double inputCostPer1K, double outputCostPer1K) {
    }
}