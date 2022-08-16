package domain.api;

import domain.catalog.EvaluationResult;
import domain.catalog.Label;

public interface LabelEvaluator {

    EvaluationResult evaluate(Label label);
}
