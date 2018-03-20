package simplegamer003.bakingapp;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import simplegamer003.bakingapp.moshihelper.Dish;

public class Home extends AppCompatActivity {

    private static final String requestUrl =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private DishViewAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = (RecyclerView) findViewById(R.id.dish_recycler_view);

        new FetchDish().execute(requestUrl);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    class FetchDish extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            try {
                return BakingDishNwUtils.getResponseFromHttpUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new Gson();
            Dish[] dish = gson.fromJson(s, Dish[].class);
            String[] dishNameArr = new String[dish.length];
            String[] dishServingsArr = new String[dish.length];

            for (int i = 0; i < dish.length; i++){
                Log.d("Dish", dish[i].toString());
                dishNameArr[i] = dish[i].getName();
                dishServingsArr[i] = String.valueOf(dish[i].getServings());
                for(int j = 0; j < dish[i].getIngredients().length; j++)
                    Log.d("Dish Ingredients", dish[i].getIngredients()[j].toString());

                for(int k = 0; k < dish[i].getSteps().length; k++)
                    Log.d("Dish Steps", dish[i].getSteps()[k].toString());
            }

            adapter = new DishViewAdapter(getApplicationContext(), dishNameArr, dishServingsArr, dish);
            recyclerView.setAdapter(adapter);
        }

    }

}
