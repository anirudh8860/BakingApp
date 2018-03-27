package simplegamer003.bakingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import me.anwarshahriar.calligrapher.Calligrapher;
import simplegamer003.bakingapp.bakedish.DishIngredientAndSteps;
import simplegamer003.bakingapp.moshihelper.Dish;

/**
 * Created by anirudhsohil on 15/03/18.
 */

public class DishViewAdapter extends RecyclerView.Adapter<DishViewAdapter.DataViewHolder>{

    private Context context;
    private String[] dishNameStr, dishServingsStr;
    private LayoutInflater inflater;
    private Dish[] dishes;
    private static final String PREF_DISH = "dish_pref";

    public class DataViewHolder extends RecyclerView.ViewHolder{

        CardView dishCardView;
        TextView dishName, dishServings;

        public DataViewHolder(View itemView) {
            super(itemView);
            dishCardView = (CardView) itemView.findViewById(R.id.dish_card_view);
            dishName = (TextView) itemView.findViewById(R.id.dish_name);
            dishServings = (TextView) itemView.findViewById(R.id.dish_servings);
        }
    }

    public DishViewAdapter(Context context, String[] dishNameStr, String[] dishServingsStr, Dish[] dishes){
        this.context = context;
        this.dishNameStr = dishNameStr;
        this.dishServingsStr = dishServingsStr;
        this.dishes = dishes;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.card_list_item_dish, parent, false);
        Calligrapher calligrapher = new Calligrapher(context);
        calligrapher.setFont(itemView, "fonts/blackjack.ttf");
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, final int position) {
        holder.dishName.setText(dishNameStr[position]);
        holder.dishServings.setText(dishServingsStr[position]);
        if (dishes[position].getImageUrl() == null || dishes[position].getImageUrl().equals("")) {
            holder.dishCardView.setBackgroundResource(getBgResource(dishNameStr[position]));
        }
        else {
            URL url = null;
            try {
                url = new URL(dishes[holder.getAdapterPosition()].getImageUrl());
                Bitmap imageBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                BitmapDrawable drawable = new BitmapDrawable(context.getResources(), imageBitmap);
                holder.dishCardView.setBackground(drawable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        holder.dishCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDishDetails(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dishNameStr != null || dishServingsStr != null)
            return Math.max(dishNameStr.length, dishServingsStr.length);
        else return 0;
    }

    private void openDishDetails(int position) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_DISH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("dish_name", dishNameStr[position]);
        editor.apply();

        Intent dishDetailsIntent = new Intent(context, DishIngredientAndSteps.class);
        dishDetailsIntent.putExtra("dish_name", dishNameStr[position]);
        dishDetailsIntent.putExtra("dish_ingredients", dishes[position].getIngredients());
        dishDetailsIntent.putExtra("dish_steps", dishes[position].getSteps());
        dishDetailsIntent.putExtra("dish_image", dishes[position].getImageUrl());
        context.startActivity(dishDetailsIntent);
    }

    private int getBgResource(String s) {
        int res = 0;
        switch (s){
            case "Nutella Pie":
                res =  R.drawable.nutella_pie;
                break;
            case "Brownies":
                res = R.drawable.brownies;
            break;
            case "Yellow Cake":
                res = R.drawable.yellow_cake;
            break;
            case "Cheesecake":
                res = R.drawable.cheese_cake;
            break;
        }
        return res;
    }
}
