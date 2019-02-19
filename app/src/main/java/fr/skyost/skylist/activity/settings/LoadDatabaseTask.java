package fr.skyost.skylist.activity.settings;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;
import java.lang.ref.WeakReference;

import fr.skyost.skylist.R;
import fr.skyost.skylist.application.SkyListApplication;
import fr.skyost.skylist.util.Utils;

/**
 * A task that allows to load a database from a file.
 */

public class LoadDatabaseTask extends AsyncTask<SettingsActivity, Void, Throwable> {

	/**
	 * The database file.
	 */

	private File databaseFile;

	/**
	 * The context.
	 */

	private WeakReference<SettingsActivity> context;

	/**
	 * Creates a new load database task.
	 *
	 * @param databaseFile The database file.
	 */

	public LoadDatabaseTask(final File databaseFile) {
		this.databaseFile = databaseFile;
	}

	@Override
	protected Throwable doInBackground(final SettingsActivity... activities) {
		try {
			// We get the application and save a reference.
			final SkyListApplication application = (SkyListApplication)activities[0].getApplication();
			this.context = new WeakReference<>(activities[0]);

			// We close the current database and we delete it.
			application.getDatabase().close();
			application.getDatabasePath(SkyListApplication.DATABASE).delete();

			// We copy the backup to the current folder.
			Utils.copy(databaseFile, application.getDatabasePath(SkyListApplication.DATABASE));

			// And we load the new database.
			application.setDatabase(SkyListApplication.DATABASE);
		}
		catch(final Exception ex) {
			return ex;
		}

		return null;
	}

	@Override
	protected void onPostExecute(final Throwable throwable) {
		super.onPostExecute(throwable);

		// We get the context.
		final SettingsActivity activity = this.context.get();
		if(activity == null) {
			return;
		}

		// If there is no error, we tell the user !
		if(throwable == null) {
			Toast.makeText(activity, R.string.toast_success, Toast.LENGTH_SHORT).show();
			activity.setNeedRestart(true);
			return;
		}

		// Otherwise, we log the error.
		throwable.printStackTrace();
		Toast.makeText(activity, activity.getString(R.string.toast_error, throwable.getClass().getName()), Toast.LENGTH_SHORT).show();
	}

	/**
	 * Returns the database file.
	 *
	 * @return The database file.
	 */

	public File getDatabaseFile() {
		return databaseFile;
	}

	/**
	 * Sets the database file.
	 *
	 * @param databaseFile The database file.
	 */

	public void setDatabaseFile(final File databaseFile) {
		this.databaseFile = databaseFile;
	}

}