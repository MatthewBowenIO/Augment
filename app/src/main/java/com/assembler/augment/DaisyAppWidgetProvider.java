package com.assembler.augment;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.RemoteViews;

/**
 * Created by Matthew on 12/14/2014.
 */
public class DaisyAppWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        SharedPreferences prefs = context.getSharedPreferences("Prefs", context.MODE_PRIVATE);

        for(int i=0; i<appWidgetIds.length; i++){
            int currentWidgetId = appWidgetIds[i];
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("IsCPURequest", 1);

            PendingIntent pending = PendingIntent.getActivity(context, 0,
                    intent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(),
                    R.layout.daisy_appwidget);
            views.setInt(R.id.WidgetButton, "setBackgroundColor", Color.parseColor(Utils.getHexForColor(prefs.getString("SelectedColor", "Green"))));
            views.setOnClickPendingIntent(R.id.WidgetButton, pending);
            appWidgetManager.updateAppWidget(currentWidgetId,views);
        }
    }
}
