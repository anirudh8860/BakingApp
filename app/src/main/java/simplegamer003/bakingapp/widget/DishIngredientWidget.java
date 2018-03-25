package simplegamer003.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import simplegamer003.bakingapp.BakingDishNwUtils;
import simplegamer003.bakingapp.DishViewAdapter;
import simplegamer003.bakingapp.Home;
import simplegamer003.bakingapp.R;
import simplegamer003.bakingapp.moshihelper.Dish;

/**
 * Implementation of App Widget functionality.
 */
public class DishIngredientWidget extends AppWidgetProvider {

    Dish[] dishes;
    private static final String requestUrl =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        Intent intent = new Intent(context, Home.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dish_ingredient_widget);
        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        new FetchDish().execute(requestUrl);

        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = updateWidgetListView(context, appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

            updateAppWidget(context, appWidgetManager, appWidgetId);git
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.dish_ingredient_widget);

        //RemoteViews Service needed to provide adapter for ListView
        Intent intent = new Intent(context, DishIngredientService.class);

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.putExtra("dishes", dishes);

        remoteViews.setRemoteAdapter(appWidgetId, intent);
        remoteViews.setEmptyView(R.id.ingredient_list_widget, R.id.empty_view);

        return remoteViews;
    }

    class FetchDish extends AsyncTask<String, Void, String> {
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
            dishes = gson.fromJson(s, Dish[].class);
            /*
            for (int i = 0; i < dish.length; i++){
                Log.d("Dish", dish[i].toString());


                for(int j = 0; j < dish[i].getIngredients().length; j++) {
                    Log.d("Dish Ingredients", dish[i].getIngredients()[j].toString());
                    dishIngredients.add(dish[i].getIngredients()[j].toString());
                }

            }
            */
        }

    }
}

