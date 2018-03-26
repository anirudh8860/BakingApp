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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import me.anwarshahriar.calligrapher.Calligrapher;
import simplegamer003.bakingapp.moshihelper.Dish;

public class Home extends AppCompatActivity {

    private static final String requestUrl =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private DishViewAdapter adapter;
    private RecyclerView recyclerView;
    private TextView notConnectedText;
    private Button retryButton;
    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "fonts/blackjack.ttf", true);

        recyclerView = (RecyclerView) findViewById(R.id.dish_recycler_view);
        notConnectedText = (TextView) findViewById(R.id.not_connected_text);
        retryButton = (Button) findViewById(R.id.check_conn_btn);
        isTablet = this.getResources().getBoolean(R.bool.isTablet);

        displayCards();

        if (isTablet)
            recyclerView.setLayoutManager(new GridLayoutManager(
                    this
                    , 2));
        else
            recyclerView.setLayoutManager(new LinearLayoutManager(
                    this
                    , LinearLayoutManager.VERTICAL
                    , false));

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayCards();
            }
        });
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
            String[] dishImageUrlArr = new String[dish.length];

            for (int i = 0; i < dish.length; i++){
                dishNameArr[i] = dish[i].getName();
                dishServingsArr[i] = String.valueOf(dish[i].getServings());
                dishImageUrlArr[i] = dish[i].getImageUrl();
            }

            adapter = new DishViewAdapter(getApplicationContext(), dishNameArr, dishServingsArr, dish);
            recyclerView.setAdapter(adapter);
        }

    }

}
