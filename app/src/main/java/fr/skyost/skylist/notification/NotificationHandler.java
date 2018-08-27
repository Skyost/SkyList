package fr.skyost.skylist.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.joda.time.DateTime;

import fr.skyost.skylist.R;
import fr.skyost.skylist.activity.SettingsActivity;
import fr.skyost.skylist.application.SkyListApplication;

/**
 * This class allows to handle the notifications sent by the application.
 */

public class NotificationHandler extends BroadcastReceiver {

	/**
	 * The notification intent parameter.
	 */

	private static final String INTENT_NOTIFICATION = "notification";

	/**
	 * The notification channel ID.
	 */

	static final String CHANNEL_ID = "fr.skyost.skylist.NOTIFICATION";

	/**
	 * Creates the application notification channel.
	 *
	 * @param context The Context.
	 */

	@RequiresApi(api = Build.VERSION_CODES.O)
	public static void createChannel(final Context context) {
		// We create a channel.
		final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, context.getString(R.string.notification_channel), NotificationManager.IMPORTANCE_DEFAULT);
		channel.enableLights(true);
		channel.enableVibration(true);
		channel.setLightColor(Color.BLUE);
		channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

		// And we add it to the notification manager.
		final NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		if(manager != null) {
			manager.createNotificationChannel(channel);
		}
	}

	@Override
	public void onReceive(final Context context, final Intent intent) {
		// We have to check whether notifications are enabled first.
		if(!areNotificationsEnabled(context)) {
			return;
		}

		// We display the notification if the user just turned on is phone or if the notification intent parameter is present.
		final boolean bootCompleted = intent.getAction() != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED);
		final boolean showNotification = intent.getBooleanExtra(INTENT_NOTIFICATION, false);
		if(!bootCompleted && !showNotification) {
			scheduleNextNotification(context);
			return;
		}

		// We creates an AsyncTask to get the To-do tasks and to send them to the user.
		final SkyListApplication application = (SkyListApplication)context.getApplicationContext();
		new NotificationDisplayer(context).execute(application.getDatabase());
	}

	/**
	 * Schedules the next notification.
	 *
	 * @param context The Context.
	 */

	public static void scheduleNextNotification(final Context context) {
		// We get a reference to the AlarmManager.
		final AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		if(manager == null) {
			return;
		}

		// We create both the Intent and the PendingIntent.
		final Intent intent = new Intent(context, NotificationHandler.class);
		intent.putExtra(INTENT_NOTIFICATION, true);
		final PendingIntent pending = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		// And we schedule the PendingIntent at tomorrow midnight (+ 1 sec, "just to be sure").
		final DateTime midnight = DateTime.now().withTimeAtStartOfDay().plusDays(1).plusSeconds(1);
		manager.set(AlarmManager.RTC_WAKEUP, midnight.getMillis(), pending);
	}

	/**
	 * Returns whether notifications are enabled according to the provided Context's SharedPreferences.
	 *
	 * @param context The Context.
	 *
	 * @return Whether notifications are enabled according to the provided Context's SharedPreferences.
	 */

	public static boolean areNotificationsEnabled(final Context context) {
		return context.getSharedPreferences(SettingsActivity.APP_PREFERENCES, Context.MODE_PRIVATE).getBoolean("app_notification", true);
	}

}