package simplegamer003.bakingapp.moshihelper;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anirudhsohil on 12/03/18.
 */

public class Dish {

    private int id;
    private String name;
    private Ingredients[] ingredients;
    private Steps[] steps;
    private int servings;
    private String image;

    public int getDishId() {
        return id;
    }

    public String getName() {
            return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Ingredients[] getIngredients(){
        return ingredients;
    }

    public void setIngredients(Ingredients[] ingredients){
        this.ingredients = ingredients;
    }

    public Steps[] getSteps(){
        return steps;
    }

    public void setSteps(Steps[] steps){
        this.steps = steps;
    }

    public int getServings() {
            return servings;
        }

    public void setServings(int servings){
        this.servings = servings;
    }

    public String getImageUrl() {
            return image;
        }

    public void setImageUrl(String image){
        this.image = image;
    }

    @Override
    public String toString() {
        StringBuffer ingredientStr = new StringBuffer(""), stepsStr = new StringBuffer("");
        for (int i = 0; i < getIngredients().length; i++)
            ingredientStr.append(getIngredients()[i] +"\n");


        for (int i = 0; i < getSteps().length; i++)
            stepsStr.append(getSteps()[i] +"\n");

        return getName() +"\n" + getServings() + "\n" + getImageUrl();
    }
}
