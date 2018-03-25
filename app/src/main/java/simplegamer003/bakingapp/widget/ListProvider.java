package simplegamer003.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.android.exoplayer2.LoadControl;

import java.util.ArrayList;

import simplegamer003.bakingapp.R;
import simplegamer003.bakingapp.moshihelper.Dish;

/**
 * Created by anirudhsohil on 25/03/18.
 */

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<ListItem> listItemList = new ArrayList<>();
    private Context context;
    private Dish[] dishes;
    private int appWidgetId;

    public ListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        dishes = (Dish[]) intent.getSerializableExtra("dishes");
        populateListItem();
    }

    private void populateListItem(){
        for (int i = 0; i < dishes.length; i++){
            ListItem listItem = new ListItem();
            listItem.heading = dishes[i].getName();
            Log.d("Dish Name", listItem.heading);

            String ingredients = "";
            for (int j = 0; j < dishes[i].getIngredients().length; j++) {
                ingredients = dishes[i].getIngredients()[j].getIngredient() + "\n";
            }
            Log.d("Ingredients List", ingredients);

            listItem.content = ingredients;
            listItemList.add(listItem);
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
        return listItemList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);

        ListItem listItem = listItemList.get(position);
        remoteView.setTextViewText(R.id.dish_name, listItem.heading);
        remoteView.setTextViewText(R.id.dish_ingredients, listItem.content);

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
