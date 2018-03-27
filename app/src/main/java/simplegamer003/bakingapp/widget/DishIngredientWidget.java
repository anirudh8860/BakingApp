package simplegamer003.bakingapp.widget;

import android.app.Dialog;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;

import simplegamer003.bakingapp.Home;
import simplegamer003.bakingapp.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class DishIngredientWidget extends AppWidgetProvider {

    private static final String PREF_DISH = "dish_pref";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_DISH, MODE_PRIVATE);
            String dishName = sharedPreferences.getString("dish_name", "Nutella Pie");
            Intent serviceIntent = new Intent(context, DishIngredientService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.putExtra("dish_name_widget", dishName);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.dish_ingredient_widget);
            remoteViews.setRemoteAdapter(R.id.ingredient_list_view_widget, serviceIntent);

            Intent activityIntent = new Intent(context, Home.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.ingredient_list_view_widget, pendingIntent);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
}

