package simplegamer003.bakingapp.widget;

import android.content.Intent;
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

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientWidgetListProvider(this.getApplicationContext(), intent);
    }
}
