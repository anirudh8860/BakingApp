package simplegamer003.bakingapp.bakedish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import simplegamer003.bakingapp.R;
import simplegamer003.bakingapp.moshihelper.Ingredients;
import simplegamer003.bakingapp.moshihelper.Steps;

public class DishIngredientAndSteps extends AppCompatActivity {

    private CardView ingredientsCard;
    private RecyclerView stepsRecyclerView;
    private ListView ingredientsList;
    private Ingredients[] ingredients;
    private Steps[] steps;
    private ViewGroup parent;
    String[] videoUrl, stepDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_ingredients_and_steps);

        Intent intent = getIntent();
        ingredientsCard = (CardView) findViewById(R.id.ingredients_card);
        ingredientsList = (ListView) findViewById(R.id.ingredients_list_view);
        stepsRecyclerView = (RecyclerView) findViewById(R.id.steps_recycler_view);
        stepsRecyclerView.setNestedScrollingEnabled(false);
        parent = (ViewGroup) findViewById(android.R.id.content);

        ingredients = (Ingredients[]) intent.getSerializableExtra("dish_ingredients");
        steps = (Steps[]) intent.getSerializableExtra("dish_steps");

        createDishDataArray();

        attachIngredientsToList();

        ingredientsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ingredientsList.getVisibility() == View.VISIBLE) {
                    ingredientsList.setVisibility(View.GONE);
                }
                else {
                    ingredientsList.setVisibility(View.VISIBLE);
                }
            }
        });

        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        addStepsToRecyclerView();
    }

    private void createDishDataArray(){
        videoUrl = new String[steps.length];
        stepDescription = new String[steps.length];

        for (int i = 0; i < steps.length; i++){
            videoUrl[i] = steps[i].getVideoURL();
            stepDescription[i] = steps[i].getDescription();
            if (videoUrl[i].equals(null) || videoUrl[i].equals(""))
                videoUrl[i] = "0";

            Log.d("Video " + i, videoUrl[i]);
            Log.d("Step Description " + i, stepDescription[i]);
        }
    }

    private void attachIngredientsToList() {
        String[] items = new String[ingredients.length];
        for (int i = 0; i < ingredients.length; i++)
            items[i] = ingredients[i].toString();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        ingredientsList.setAdapter(adapter);
    }

    private void addStepsToRecyclerView(){
        String[] items = new String[steps.length];
        for (int i = 0; i < steps.length; i++) {
            items[i] = steps[i].getShortDescription();
        }

        DishStepsViewAdapter adapter = new DishStepsViewAdapter(this, items, steps);
        stepsRecyclerView.setAdapter(adapter);
        setListViewHeightBasedOnItems(ingredientsList, parent);
    }

    public static int setListViewHeightBasedOnItems(ListView listView, ViewGroup container) {

        ListAdapter listAdapter = listView.getAdapter();

        int numberOfItems = listAdapter.getCount();

        // Get total height of all items.
        int totalItemsHeight = 0;
        for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
            View item = listAdapter.getView(itemPos, null, listView);
            item.measure(0, 0);
            totalItemsHeight += item.getMeasuredHeight();
        }

        // Get total height of all item dividers.
        int totalDividersHeight = listView.getDividerHeight() * (numberOfItems - 1);

        // Set list height.
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalItemsHeight + totalDividersHeight;
        listView.setLayoutParams(params);
        listView.requestLayout();

        //redraw the container layout.
        container.requestLayout();

        return params.height;
    }
}
