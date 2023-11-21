package com.ricardoocorreia.projects.domain.impl;

import com.ricardoocorreia.projects.domain.api.LabelEvaluatorUseCase;
import com.ricardoocorreia.projects.domain.catalog.EvaluationResult;
import com.ricardoocorreia.projects.domain.catalog.Label;
import com.ricardoocorreia.projects.domain.catalog.LabelLimits;
import com.ricardoocorreia.projects.domain.catalog.Measurement;
import com.ricardoocorreia.projects.domain.catalog.Nutrient;
import com.ricardoocorreia.projects.domain.catalog.NutrientScore;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LabelEvaluatorUseCaseImpl implements LabelEvaluatorUseCase {

    private final Configuration configuration;

    @Override
    public EvaluationResult evaluate(Label label, Measurement measurement) {

        return new EvaluationResult(label.getNutrients()
                .entrySet()
                .stream()
                .map(nutrientFloatEntry ->
                        new NutrientValue(nutrientFloatEntry.getKey(),
                                nutrientFloatEntry.getValue(),
                                new LabelLimits.Key(nutrientFloatEntry.getKey(), measurement)))
                .map(nutrientValue -> calculateScore(nutrientValue.nutrient(), nutrientValue.value(), configuration.getLabelLimit(nutrientValue.labelLimitsKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    private Map.Entry<Nutrient, NutrientScore> calculateScore(Nutrient nutrient, float value, LabelLimits.Limits labelLimit) {

        if (value < labelLimit.lower()) {
            return Map.entry(nutrient, NutrientScore.GREEN);
        } else if (value > labelLimit.upper()) {
            return Map.entry(nutrient, NutrientScore.RED);
        } else {
            return Map.entry(nutrient, NutrientScore.YELLOW);
        }
    }

    private record NutrientValue(Nutrient nutrient, float value, LabelLimits.Key labelLimitsKey) {
    }
}
