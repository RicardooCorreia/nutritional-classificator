package com.ricardoocorreia.projects.domain.api;

import com.ricardoocorreia.projects.domain.catalog.EvaluationResult;
import com.ricardoocorreia.projects.domain.catalog.Label;
import com.ricardoocorreia.projects.domain.catalog.LabelLimits;
import com.ricardoocorreia.projects.domain.catalog.Measurement;

public interface LabelEvaluatorUseCase {

    EvaluationResult evaluate(Label label, Measurement measurement);

    interface Configuration {

        LabelLimits.Limits getLabelLimit(LabelLimits.Key labelKey);
    }
}
