package com.example.appwidgetsample;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.new_app_widget);
        int batteryLevel = NewAppWidget.getBatteryLevelPct(context);
        remoteViews.setTextViewText(R.id.appwidget_id, batteryLevel + "%");
        ComponentName thisWidget = new ComponentName(context, NewAppWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget, remoteViews);
        Log.d("XXXXXXXXXXXXXX", "onReceive: ABC");
    }
}
