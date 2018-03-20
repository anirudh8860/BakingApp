package simplegamer003.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, final int position) {
        holder.dishName.setText(dishNameStr[position]);
        holder.dishServings.setText(dishServingsStr[position]);
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
        Intent dishDetailsIntent = new Intent(context, DishIngredientAndSteps.class);
        dishDetailsIntent.putExtra("dish_ingredients", dishes[position].getIngredients());
        dishDetailsIntent.putExtra("dish_steps", dishes[position].getSteps());
        context.startActivity(dishDetailsIntent);
    }
}
