package fr.skyost.skylist.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import org.joda.time.DateTime;

import fr.skyost.skylist.R;
import fr.skyost.skylist.activity.MainActivity;

/**
 * The app widget provider.
 */

public class WidgetProvider extends AppWidgetProvider {

	/**
	 * The intent parameter that allows to refresh the widget by sending a broadcast to this receiver.
	 */

	public static final String INTENT_REFRESH = "refresh";

	@Override
	public void onReceive(final Context context, final Intent intent) {
		// If the intent has the good parameter, we can refresh the widget.
		if(intent.hasExtra(INTENT_REFRESH)) {
			final AppWidgetManager manager = AppWidgetManager.getInstance(context);
			this.onUpdate(
					context,
					manager,
					intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID) ? new int[]{intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)} : manager.getAppWidgetIds(new ComponentName(context, this.getClass()))
			);
		}

		super.onReceive(context, intent);
	}

	@Override
	public final void onUpdate(final Context context, final AppWidgetManager manager, final int[] ids) {
		// Updates messages, intents, etc... Then sends the update and schedules the next update.
		final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		updateMessages(context, views);
		registerIntents(context, views);
		for(final int id : ids) {
			manager.notifyAppWidgetViewDataChanged(id, R.id.widget_todo);
			manager.updateAppWidget(id, views);
		}
		scheduleNextUpdate(context);

		super.onUpdate(context, manager, ids);
	}

	/**
	 * Update widgets' messages.
	 *
	 * @param context The context.
	 * @param views Widgets' RemoteViews.
	 */

	public final void updateMessages(final Context context, final RemoteViews views) {
		final Intent intent = new Intent(context, WidgetService.class);
		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		views.setRemoteAdapter(R.id.widget_todo, intent);
	}

	/**
	 * Registers all needed intents to the specified RemoteView.
	 *
	 * @param context The Context.
	 * @param views Widgets' RemoteViews.
	 */

	public final void registerIntents(final Context context, final RemoteViews views) {
		views.setOnClickPendingIntent(R.id.widget_title, PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0));
	}

	/**
	 * Schedules widgets next update.
	 *
	 * @param context The Context.
	 */

	public final void scheduleNextUpdate(final Context context) {
		// We get a reference to the AlarmManager.
		final AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		if(manager == null) {
			return;
		}

		// We create both the Intent and the PendingIntent.
		final PendingIntent pending = PendingIntent.getBroadcast(context, 0, new Intent(context, TriggerWidgetUpdate.class), PendingIntent.FLAG_UPDATE_CURRENT);

		// And we schedule the PendingIntent at tomorrow midnight (+ 1 sec, "just to be sure").
		final DateTime midnight = DateTime.now().withTimeAtStartOfDay().plusDays(1).plusSeconds(1);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			manager.setExact(AlarmManager.RTC_WAKEUP, midnight.getMillis(), pending);
		}
		else {
			manager.set(AlarmManager.RTC_WAKEUP, midnight.getMillis(), pending);
		}
	}

}
