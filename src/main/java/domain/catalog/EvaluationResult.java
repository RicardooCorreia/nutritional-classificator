package domain.catalog;

import java.util.Map;

public record EvaluationResult(Map<Nutrient, NutrientScore> nutrientScores) {
}
