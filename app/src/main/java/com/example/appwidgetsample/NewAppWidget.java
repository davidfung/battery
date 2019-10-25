package com.example.appwidgetsample;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    static void updateWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Compile the last update time string
        String dateString = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());
        String lastUpdateString = context.getResources().getString(R.string.date_count_format, dateString);

        // Construct the RemoteViews object and update the content
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.appwidget_id, String.valueOf(appWidgetId));
        views.setTextViewText(R.id.appwidget_update, lastUpdateString);

        Log.d("AMG99", "updateAppWidget() " + lastUpdateString);

        // override the widget id with battery level
        int batteryPct = getBatteryLevelPct(context);
        if (batteryPct != (-1)) {
            views.setTextViewText(R.id.appwidget_id, batteryPct + "%");
        }
        Log.d("AMG99", "updateAppWidget() " + batteryPct + "%");

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    static int getBatteryLevelPct(Context context) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, filter);
        int level;
        int scale;
        int batteryPct = (-1);
        if (batteryStatus != null) {
            level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            batteryPct = Math.round(level / (float) scale * 100);
        }
        return batteryPct;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        Log.d("AMG99", "onEnabled()");

        // Create an Intent to update the widget
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Set a repeating alarm to call the pending intent
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + 100, 60000, pi);
    }

    @Override
    public void onDisabled(Context context) {
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
        super.onDisabled(context);
    }
}

