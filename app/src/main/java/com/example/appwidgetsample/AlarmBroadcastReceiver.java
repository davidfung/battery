package com.example.appwidgetsample;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.util.Date;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Compile the last update time string
        String dateString = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());
        String lastUpdateString = context.getResources().getString(R.string.date_count_format, dateString);

        // Construct the RemoteViews object and update the content
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.appwidget_id, "BAT");
        views.setTextViewText(R.id.appwidget_update, lastUpdateString);

        Log.d("AMG99", "Alarm onReceive() " + lastUpdateString);

        // Tell the AppWidgetManager to perform an update on the widget
        ComponentName thisWidget = new ComponentName(context, NewAppWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget, views);
    }
}
