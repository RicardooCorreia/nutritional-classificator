package com.ricardoocorreia.projects.domain.impl;

import com.github.javafaker.Faker;
import com.ricardoocorreia.projects.domain.api.LabelEvaluatorUseCase;
import com.ricardoocorreia.projects.domain.catalog.EvaluationResult;
import com.ricardoocorreia.projects.domain.catalog.Label;
import com.ricardoocorreia.projects.domain.catalog.LabelLimits;
import com.ricardoocorreia.projects.domain.catalog.Measurement;
import com.ricardoocorreia.projects.domain.catalog.Nutrient;
import com.ricardoocorreia.projects.domain.catalog.NutrientScore;
import io.quarkus.test.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class LabelEvaluatorUseCaseImplTest {

    @Mock
    private LabelEvaluatorUseCase.Configuration configuration;

    private LabelEvaluatorUseCaseImpl subject;

    private final Faker faker = new Faker();

    @BeforeEach
    void setUp() {

        configuration = mock(LabelEvaluatorUseCase.Configuration.class);
        subject = new LabelEvaluatorUseCaseImpl(configuration);
    }

    @Test
    void evaluate_whenAllValuesAreGoodValue_returnGoodResult() {

        // Given
        final float fat = faker.number().randomDigitNotZero();
        final float saturatedFat = faker.number().randomDigitNotZero();
        final float sugar = faker.number().randomDigitNotZero();
        final float salt = faker.number().randomDigitNotZero();
        final Label label = new Label(fat, saturatedFat, sugar, salt);
        final Measurement measurement = Measurement.GRAM;


        final LabelLimits.Limits fatLimits = underLowerLimit(fat);
        doReturn(fatLimits)
                .when(configuration)
                .getLabelLimit(eq(new LabelLimits.Key(Nutrient.FAT, measurement)));

        final LabelLimits.Limits saturatedFatLimits = underLowerLimit(saturatedFat);
        doReturn(saturatedFatLimits)
                .when(configuration)
                .getLabelLimit(eq(new LabelLimits.Key(Nutrient.SATURATED_FAT, measurement)));

        final LabelLimits.Limits sugarLimits = underLowerLimit(sugar);
        doReturn(sugarLimits)
                .when(configuration)
                .getLabelLimit(eq(new LabelLimits.Key(Nutrient.SUGAR, measurement)));

        final LabelLimits.Limits saltLimits = underLowerLimit(salt);
        doReturn(saltLimits)
                .when(configuration)
                .getLabelLimit(eq(new LabelLimits.Key(Nutrient.SALT, measurement)));

        // When
        final EvaluationResult result = subject.evaluate(label, measurement);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.nutrientScores())
                .containsExactlyInAnyOrderEntriesOf(
                        Map.of(
                                Nutrient.FAT, NutrientScore.GREEN,
                                Nutrient.SATURATED_FAT, NutrientScore.GREEN,
                                Nutrient.SUGAR, NutrientScore.GREEN,
                                Nutrient.SALT, NutrientScore.GREEN
                        ));
    }

    @Test
    void evaluate_whenAllValuesAreMediumValue_returnYellowResult() {

        // Given
        final float fat = faker.number().randomDigitNotZero();
        final float saturatedFat = faker.number().randomDigitNotZero();
        final float sugar = faker.number().randomDigitNotZero();
        final float salt = faker.number().randomDigitNotZero();
        final Label label = new Label(fat, saturatedFat, sugar, salt);
        final Measurement measurement = Measurement.MILLILITER;


        final LabelLimits.Limits fatLimits = overLowerLimitUnderUpperLimit(fat);
        doReturn(fatLimits)
                .when(configuration)
                .getLabelLimit(eq(new LabelLimits.Key(Nutrient.FAT, measurement)));

        final LabelLimits.Limits saturatedFatLimits = overLowerLimitUnderUpperLimit(saturatedFat);
        doReturn(saturatedFatLimits)
                .when(configuration)
                .getLabelLimit(eq(new LabelLimits.Key(Nutrient.SATURATED_FAT, measurement)));

        final LabelLimits.Limits sugarLimits = overLowerLimitUnderUpperLimit(sugar);
        doReturn(sugarLimits)
                .when(configuration)
                .getLabelLimit(eq(new LabelLimits.Key(Nutrient.SUGAR, measurement)));

        final LabelLimits.Limits saltLimits = overLowerLimitUnderUpperLimit(salt);
        doReturn(saltLimits)
                .when(configuration)
                .getLabelLimit(eq(new LabelLimits.Key(Nutrient.SALT, measurement)));

        // When
        final EvaluationResult result = subject.evaluate(label, measurement);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.nutrientScores())
                .containsExactlyInAnyOrderEntriesOf(
                        Map.of(
                                Nutrient.FAT, NutrientScore.YELLOW,
                                Nutrient.SATURATED_FAT, NutrientScore.YELLOW,
                                Nutrient.SUGAR, NutrientScore.YELLOW,
                                Nutrient.SALT, NutrientScore.YELLOW
                        ));
    }

    @Test
    void evaluate_whenAllValuesAreBadValue_returnRedResult() {

        // Given
        final float fat = faker.number().randomDigitNotZero();
        final float saturatedFat = faker.number().randomDigitNotZero();
        final float sugar = faker.number().randomDigitNotZero();
        final float salt = faker.number().randomDigitNotZero();
        final Label label = new Label(fat, saturatedFat, sugar, salt);
        final Measurement measurement = Measurement.MILLILITER;


        final LabelLimits.Limits fatLimits = overUpperLimit(fat);
        doReturn(fatLimits)
                .when(configuration)
                .getLabelLimit(eq(new LabelLimits.Key(Nutrient.FAT, measurement)));

        final LabelLimits.Limits saturatedFatLimits = overUpperLimit(saturatedFat);
        doReturn(saturatedFatLimits)
                .when(configuration)
                .getLabelLimit(eq(new LabelLimits.Key(Nutrient.SATURATED_FAT, measurement)));

        final LabelLimits.Limits sugarLimits = overUpperLimit(sugar);
        doReturn(sugarLimits)
                .when(configuration)
                .getLabelLimit(eq(new LabelLimits.Key(Nutrient.SUGAR, measurement)));

        final LabelLimits.Limits saltLimits = overUpperLimit(salt);
        doReturn(saltLimits)
                .when(configuration)
                .getLabelLimit(eq(new LabelLimits.Key(Nutrient.SALT, measurement)));

        // When
        final EvaluationResult result = subject.evaluate(label, measurement);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.nutrientScores())
                .containsExactlyInAnyOrderEntriesOf(
                        Map.of(
                                Nutrient.FAT, NutrientScore.RED,
                                Nutrient.SATURATED_FAT, NutrientScore.RED,
                                Nutrient.SUGAR, NutrientScore.RED,
                                Nutrient.SALT, NutrientScore.RED
                        ));
    }

    @Test
    void evaluate_whenValuesAreMixedValue_returnMixedResult() {

        // Given
        final float fat = faker.number().randomDigitNotZero();
        final float saturatedFat = faker.number().randomDigitNotZero();
        final float sugar = faker.number().randomDigitNotZero();
        final float salt = faker.number().randomDigitNotZero();
        final Label label = new Label(fat, saturatedFat, sugar, salt);
        final Measurement measurement = Measurement.MILLILITER;


        final LabelLimits.Limits fatLimits = overUpperLimit(fat);
        doReturn(fatLimits)
                .when(configuration)
                .getLabelLimit(eq(new LabelLimits.Key(Nutrient.FAT, measurement)));

        final LabelLimits.Limits saturatedFatLimits = underLowerLimit(saturatedFat);
        doReturn(saturatedFatLimits)
                .when(configuration)
                .getLabelLimit(eq(new LabelLimits.Key(Nutrient.SATURATED_FAT, measurement)));

        final LabelLimits.Limits sugarLimits = overLowerLimitUnderUpperLimit(sugar);
        doReturn(sugarLimits)
                .when(configuration)
                .getLabelLimit(eq(new LabelLimits.Key(Nutrient.SUGAR, measurement)));

        final LabelLimits.Limits saltLimits = overLowerLimitUnderUpperLimit(salt);
        doReturn(saltLimits)
                .when(configuration)
                .getLabelLimit(eq(new LabelLimits.Key(Nutrient.SALT, measurement)));

        // When
        final EvaluationResult result = subject.evaluate(label, measurement);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.nutrientScores())
                .containsExactlyInAnyOrderEntriesOf(
                        Map.of(
                                Nutrient.FAT, NutrientScore.RED,
                                Nutrient.SATURATED_FAT, NutrientScore.GREEN,
                                Nutrient.SUGAR, NutrientScore.YELLOW,
                                Nutrient.SALT, NutrientScore.YELLOW
                        ));
    }

    private LabelLimits.Limits underLowerLimit(float value) {

        return new LabelLimits.Limits(value + 0.5f, value + 0.3f);
    }

    private LabelLimits.Limits overLowerLimitUnderUpperLimit(float value) {

        return new LabelLimits.Limits(value + 0.5f, value - 0.3f);
    }

    private LabelLimits.Limits overUpperLimit(float value) {

        return new LabelLimits.Limits(value - 0.3f, value - 0.5f);
    }
}
