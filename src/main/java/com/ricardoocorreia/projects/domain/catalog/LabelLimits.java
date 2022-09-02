package com.ricardoocorreia.projects.domain.catalog;

public record LabelLimits(Key key, Limits limits) {

    public record Key(Nutrient nutrient, Measurement measurement) {
    }

    public record Limits(float upper, float lower) {
    }
}
