package com.example.appwidgetsample;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    private static final String mSharedPrefFile = "com.example.android.appwidgetsample";
    private static final String COUNT_KEY = "count";

    static void updateWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

         // Increment the count
        SharedPreferences prefs = context.getSharedPreferences(
                mSharedPrefFile, 0);
        int count = prefs.getInt(COUNT_KEY + appWidgetId, 0);
        count++;

        // Save the count
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(COUNT_KEY + appWidgetId, count);
        prefEditor.apply();

        // Compile the last update time string
        String dateString =
                DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());
        String lastUpdateString = context.getResources().getString(
                R.string.date_count_format, count, dateString);

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

        // Setup update button to send an update request as a pending intent.
        // The intent action must be an app widget update.
        // Include the widget ID to be updated as an intent extra.
        // Wrap it all in a pending intent to send a broadcast.
        // Use the app widget ID as the request code (third argument) so that
        // each intent is unique.
        // Assign the pending intent to the button onClick handler
        Intent intent = new Intent(context, NewAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] idArray = new int[]{appWidgetId};
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);
        PendingIntent pi = PendingIntent.getBroadcast(context,
                appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.button_update, pi);
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

        // Create an Intent to update the widget
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Set a repeating alarm to call the pending intent
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + 100 * 3, 60000 * 10, pi);
    }

}

