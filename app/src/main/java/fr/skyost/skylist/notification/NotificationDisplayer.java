package fr.skyost.skylist.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import org.joda.time.LocalDate;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import fr.skyost.skylist.R;
import fr.skyost.skylist.activity.MainActivity;
import fr.skyost.skylist.task.TodoTask;
import fr.skyost.skylist.task.database.SkyListDatabase;

/**
 * This task allows to displays notifications according to today's tasks.
 */

public class NotificationDisplayer extends AsyncTask<SkyListDatabase, Void, Collection<TodoTask>> {

	/**
	 * The current Context reference.
	 */

	private final AtomicReference<Context> context = new AtomicReference<>();

	NotificationDisplayer(final Context context) {
		this.context.set(context);
	}

	@Override
	protected Collection<TodoTask> doInBackground(final SkyListDatabase... skyListDatabases) {
		return skyListDatabases[0].getTodoTaskDao().queryTasksForDate(LocalDate.now());
	}

	@Override
	protected void onPostExecute(final Collection<TodoTask> tasks) {
		// We check if there are tasks today and if the Context is still present.
		final Context context = this.context.get();
		if(context == null || tasks.isEmpty()) {
			return;
		}

		// We get a reference to the notification manager.
		final NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		if(manager == null) {
			return;
		}

		// We add the task to the notification.
		final NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
		for(final TodoTask task : tasks) {
			style.addLine("- " + task.getDescription());
		}

		// We then send the notification (and we schedule the next one)
		final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationHandler.CHANNEL_ID)
				.setSmallIcon(R.drawable.notification_icon)
				.setContentTitle(context.getString(R.string.notification_title))
				.setContentText(context.getString(R.string.notification_content, tasks.size()))
				.setStyle(style)
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				.setNumber(tasks.size())
				.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0));

		manager.notify(0, builder.build());
		NotificationHandler.scheduleNextNotification(context);
	}

	/**
	 * Returns the context reference.
	 *
	 * @return The context reference.
	 */

	public AtomicReference<Context> getContextReference() {
		return context;
	}

}