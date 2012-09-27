package com.nevercom.android.uptodate;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class HomescreenWidget extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		for (int i = 0; i < appWidgetIds.length; i++) {
			int appWidgetId = appWidgetIds[i];
			
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			Intent intent = new Intent(context, MainActivity.class);
			//intent.putExtra("search", true);
			PendingIntent pending = PendingIntent.getActivity(context, 0, intent, 0);
			views.setOnClickPendingIntent(R.id.imageView3, pending);
			
			appWidgetManager.updateAppWidget(appWidgetId, views);
			
		}
	}
	

}
