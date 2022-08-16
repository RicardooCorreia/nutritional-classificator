package domain.catalog;

public interface LabelLimits {

    float getUpperLimit();

    float getLowerLimit();

    Measurement getMeasurement();

    Nutrient getNutrient();
}
