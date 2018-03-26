package simplegamer003.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import simplegamer003.bakingapp.R;
import simplegamer003.bakingapp.moshihelper.Dish;
import simplegamer003.bakingapp.moshihelper.Ingredients;

/**
 * Created by anirudhsohil on 25/03/18.
 */

public class WidgetListProvider implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private Dish[] dishes;
    private ArrayList<Dish> dishArrayList;

    public WidgetListProvider(Context context, Intent intent) {
        this.context = context;
        dishes = getDishFromJson(context);
    }

    private Dish[] getDishFromJson(Context context){
        String json = "";
        try {
            InputStream stream = context.getAssets().open("dish.json");
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            json = new String(buffer, "UTF-8");
            Gson gson = new Gson();
            Dish[] dish = gson.fromJson(json, Dish[].class);
            return dish;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return dishes.length;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);

        String ingredientsStr = "";
        for (int j = 0; j < dishes[position].getIngredients().length; j++) {
            Ingredients ingredients = dishes[position].getIngredients()[j];
            ingredientsStr += "* " + ingredients.getQuantity() + ingredients.getMeasure() + " " + ingredients.getIngredient() + "\n";
        }

        Log.d("Ingredients", ingredientsStr);

        remoteView.setTextViewText(R.id.dish_name, dishes[position].getName());
        remoteView.setTextViewText(R.id.dish_ingredients, ingredientsStr);

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
