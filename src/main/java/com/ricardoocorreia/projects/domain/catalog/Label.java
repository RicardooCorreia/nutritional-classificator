package com.ricardoocorreia.projects.domain.catalog;

import java.util.Map;

public record Label(float fat, float saturatedFat, float sugar, float salt) {

    public Map<Nutrient, Float> getNutrients() {

        return Map.of(
                Nutrient.FAT, fat,
                Nutrient.SATURATED_FAT, saturatedFat,
                Nutrient.SUGAR, sugar,
                Nutrient.SALT, salt
        );
    }
}
