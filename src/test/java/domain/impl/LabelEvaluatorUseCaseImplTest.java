package domain.impl;

import com.github.javafaker.Faker;
import domain.api.LabelEvaluatorUseCase;
import domain.catalog.EvaluationResult;
import domain.catalog.Label;
import domain.catalog.LabelLimits;
import domain.catalog.Measurement;
import domain.catalog.Nutrient;
import domain.catalog.NutrientScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

class LabelEvaluatorUseCaseImplTest {

    @Mock
    private LabelEvaluatorUseCase.Configuration configuration;

    private LabelEvaluatorUseCaseImpl subject;

    private final Faker faker = new Faker();

    @BeforeEach
    void setUp() {

        openMocks(this);
        subject = new LabelEvaluatorUseCaseImpl(configuration);
    }

    @Test
    void evaluate_whenAllValuesAreGoodValue_returnGoodResult() {

        // Given
        final float fat = faker.number().randomNumber();
        final float saturatedFat = faker.number().randomNumber();
        final float sugar = faker.number().randomNumber();
        final float salt = faker.number().randomNumber();
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

    private LabelLimits.Limits underLowerLimit(float fat) {

        return new LabelLimits.Limits(fat + 0.5f, fat + 0.3f);
    }
}
