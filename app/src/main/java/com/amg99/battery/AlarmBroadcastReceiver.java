package com.amg99.battery;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import static com.amg99.battery.BatteryWidget.updateViews;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Update the remote view with the current battery level
        RemoteViews views = updateViews(context);

        // Tell the AppWidgetManager to perform an update on the widget
        ComponentName thisWidget = new ComponentName(context, BatteryWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget, views);
    }
}