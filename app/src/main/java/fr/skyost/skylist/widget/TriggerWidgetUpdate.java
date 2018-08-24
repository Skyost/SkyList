package fr.skyost.skylist.widget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This receiver allows to trigger a widget update.
 */

public class TriggerWidgetUpdate extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, final Intent intent) {
		final Intent updateIntent = new Intent(context, WidgetProvider.class);
		updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		updateIntent.putExtra(WidgetProvider.INTENT_REFRESH, true);
		context.sendBroadcast(updateIntent);
	}

}