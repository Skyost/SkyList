package fr.skyost.skylist.activity.settings;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.lang.ref.WeakReference;

import fr.skyost.skylist.R;
import fr.skyost.skylist.application.SkyListApplication;
import fr.skyost.skylist.util.Utils;

/**
 * A task that allows to save a database to a file.
 */

public class SaveDatabaseTask extends AsyncTask<SkyListApplication, Void, Throwable> {

	/**
	 * The context.
	 */

	private WeakReference<Context> context;

	@Override
	protected Throwable doInBackground(final SkyListApplication... applications) {
		try {
			// We get the application and save a reference.
			final SkyListApplication application = applications[0];
			this.context = new WeakReference<>(application);

			// We close the current database and we copy it.
			application.getDatabase().close();

			final File source = application.getDatabasePath(SkyListApplication.DATABASE);
			final File destinationDirectory = getBackupsDirectory();
			destinationDirectory.mkdirs();

			final File destination = new File(destinationDirectory, "backup-" + System.currentTimeMillis() + ".db");
			Utils.copy(source, destination);

			// And we load the database again.
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
		final Context context = this.context.get();
		if(context == null) {
			return;
		}

		// If there is no error, we tell the user !
		if(throwable == null) {
			Toast.makeText(context, R.string.toast_success, Toast.LENGTH_SHORT).show();
			return;
		}

		// Otherwise, we log the error.
		throwable.printStackTrace();
		Toast.makeText(context, context.getString(R.string.toast_error, throwable.getClass().getName()), Toast.LENGTH_SHORT).show();
	}

	/**
	 * Returns the backup directory.
	 *
	 * @return The backup directory.
	 */

	static File getBackupsDirectory() {
		return new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "SkyList" + File.separator + "Backups");
	}

}