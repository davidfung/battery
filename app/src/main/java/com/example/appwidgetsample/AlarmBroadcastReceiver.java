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

        // get the layout for the widget and update the battery percentage
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.new_app_widget);
        ////remoteViews.setTextViewText(R.id.appwidget_id, "CAT");
        // Compile the last update time string
        String dateString =
                DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());
        String lastUpdateString = context.getResources().getString(
                R.string.date_count_format, 0, dateString);
        ////remoteViews.setTextViewText(R.id.button_update, lastUpdateString);
        remoteViews.setTextViewText(R.id.appwidget_id, lastUpdateString);

        Log.d("AMG99", "Alarm onReceive() " + lastUpdateString);

        // Tell the AppWidgetManager to perform an update on the widget
        ComponentName thisWidget = new ComponentName(context, NewAppWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget, remoteViews);
    }
}
