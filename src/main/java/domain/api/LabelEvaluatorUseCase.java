package domain.api;

import domain.catalog.EvaluationResult;
import domain.catalog.Label;
import domain.catalog.LabelLimits;
import domain.catalog.Measurement;

public interface LabelEvaluatorUseCase {

    EvaluationResult evaluate(Label label, Measurement measurement);

    interface Configuration {

        LabelLimits.Limits getLabelLimit(LabelLimits.Key labelKey);
    }
}
