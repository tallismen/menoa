package nl.anwb.menoa.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import nl.anwb.menoa.R;
import nl.anwb.menoa.ui.GameActivity;

/**
 * De widgetprovider die de Easteregg start
 */
public class NoodAppWidgetProvider extends AppWidgetProvider {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        log.info("Updating widgets " + Arrays.asList(appWidgetIds));

        // Perform this loop procedure for each App Widget that belongs to this
        // provider
        for (int appWidgetId : appWidgetIds) {
            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, GameActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget1);
            views.setOnClickPendingIntent(R.id.widgetwegenwacht, pendingIntent);
            //views.setOnClickPendingIntent(R.id.button6, pendingIntent);
            //views.setOnClickPendingIntent(R.id.button5, pendingIntent);

            // To update a label

            // Tell the AppWidgetManager to perform an update on the current app
            // widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

}
