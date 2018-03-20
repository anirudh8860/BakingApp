package simplegamer003.bakingapp.moshihelper;

import java.io.Serializable;

/**
 * Created by anirudhsohil on 12/03/18.
 */

public class Ingredients implements Serializable {

    float quantity;
    String measure;
    String ingredient;

    public float getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    @Override
    public String toString() {
        return getQuantity() + " " + getMeasure() + " " + getIngredient();
    }
}
