package domain.impl;

import domain.api.LabelEvaluatorUseCase;
import domain.catalog.EvaluationResult;
import domain.catalog.Label;
import domain.catalog.Measurement;
import domain.catalog.Nutrient;
import domain.catalog.NutrientScore;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class LabelEvaluatorUseCaseImpl implements LabelEvaluatorUseCase {

    private final LabelEvaluatorUseCase.Configuration configuration;

    @Override
    public EvaluationResult evaluate(Label label, Measurement measurement) {

        return new EvaluationResult(Map.of(
                Nutrient.FAT, NutrientScore.GREEN,
                Nutrient.SATURATED_FAT, NutrientScore.GREEN,
                Nutrient.SALT, NutrientScore.GREEN,
                Nutrient.SUGAR, NutrientScore.GREEN
        ));
    }
}
