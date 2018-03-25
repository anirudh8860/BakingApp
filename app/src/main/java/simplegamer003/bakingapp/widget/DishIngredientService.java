package simplegamer003.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.RemoteViewsService;

/**
 * Created by anirudhsohil on 25/03/18.
 */

public class DishIngredientService extends RemoteViewsService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * Used to name the worker thread, important only for debugging.
     */

    public static final String ACTION_SHOW_INGREDIENTS = "simplegamer003.bakingapp.action.show_ingredients";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        if (intent != null){
            int appId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            return new ListProvider(this.getApplicationContext(), intent);
        }
        return null;
    }
}
