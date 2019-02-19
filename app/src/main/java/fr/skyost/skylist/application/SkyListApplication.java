package fr.skyost.skylist.application;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Build;

import org.joda.time.LocalDate;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import fr.skyost.skylist.R;
import fr.skyost.skylist.application.theme.SkyListTheme;
import fr.skyost.skylist.notification.NotificationHandler;
import fr.skyost.skylist.task.TodoTask;
import fr.skyost.skylist.task.database.SkyListDatabase;

/**
 * The SkyList application class.
 */

public class SkyListApplication extends Application {

	/**
	 * The database file.
	 */

	public static final String DATABASE = "app_data.db";

	/**
	 * The application database.
	 */

	private SkyListDatabase database;

	/**
	 * The current application theme.
	 */

	private SkyListTheme theme;

	@Override
	public void onCreate() {
		super.onCreate();

		// Loads the database and the theme.
		database = Room.databaseBuilder(this, SkyListDatabase.class, DATABASE)
				.addCallback(new RoomDatabase.Callback() {

					@Override
					public void onCreate(@NonNull final SupportSQLiteDatabase db) {
						super.onCreate(db);

						AsyncTask.execute(() -> database.getTodoTaskDao().addTask(new TodoTask(getString(R.string.main_todo_first), LocalDate.now(), getResources().getIntArray(R.array.todo_task_available_colors)[0])));
					}

				})
				.build();
		theme = SkyListTheme.load(this);

		// Setups the notifications.
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationHandler.createChannel(this);
		}
		if(NotificationHandler.areNotificationsEnabled(this)) {
			NotificationHandler.scheduleNextNotification(this);
		}
	}

	/**
	 * Returns the database.
	 *
	 * @return The database.
	 */

	public SkyListDatabase getDatabase() {
		return database;
	}

	/**
	 * Sets the database.
	 *
	 * @param database The database.
	 */

	public void setDatabase(final String database) {
		this.database = Room.databaseBuilder(this, SkyListDatabase.class, database).build();
	}

	/**
	 * Returns the current applied theme.
	 *
	 * @return The current applied theme.
	 */

	public SkyListTheme getSkyListTheme() {
		return theme;
	}

}