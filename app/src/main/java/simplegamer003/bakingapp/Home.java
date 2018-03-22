package simplegamer003.bakingapp;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import simplegamer003.bakingapp.moshihelper.Dish;

public class Home extends AppCompatActivity {

    private static final String requestUrl =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private DishViewAdapter adapter;
    private RecyclerView recyclerView;
    private TextView notConnectedText;
    private Button retryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = (RecyclerView) findViewById(R.id.dish_recycler_view);
        notConnectedText = (TextView) findViewById(R.id.not_connected_text);
        retryButton = (Button) findViewById(R.id.check_conn_btn);

        displayCards();

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void displayCards() {
        if (isNetworkAvailable()) {
            notConnectedText.setVisibility(View.GONE);
            retryButton.setVisibility(View.GONE);
            new FetchDish().execute(requestUrl);
        }
        else
            notConnectedText.setText(R.string.not_connected_text);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
