package domain.impl;

import domain.api.LabelEvaluatorUseCase;
import domain.catalog.EvaluationResult;
import domain.catalog.Label;
import domain.catalog.LabelLimits;
import domain.catalog.Measurement;
import domain.catalog.Nutrient;
import domain.catalog.NutrientScore;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LabelEvaluatorUseCaseImpl implements LabelEvaluatorUseCase {

    private final LabelEvaluatorUseCase.Configuration configuration;

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
