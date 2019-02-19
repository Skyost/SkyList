package fr.skyost.skylist.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import fr.skyost.skylist.task.adapter.TodoListAdapterItem;
import fr.skyost.skylist.task.adapter.classifier.date.DateClassifier;

/**
 * This class contains some useful methods.
 */

public class Utils {

	/**
	 * Returns the closest element in the list using compareTo(...).
	 *
	 * @param target The target element.
	 * @param list The list.
	 *
	 * @return The closest element.
	 */

	public static int getClosestElementPosition(final DateClassifier target, final List<TodoListAdapterItem> list) {
		// We keep a reference to the target day (in millis).
		final long targetDay = target.getDate().toDateTimeAtStartOfDay().getMillis();

		// We save the last data (compareTo & position).
		long compareToResult = -1;
		int lastPosition = -1;

		// We iterate through the list.
		for(int i = 0; i < list.size(); i++) {
			// We have to check that the element is a Classifier.
			final TodoListAdapterItem element = list.get(i);
			if(!(element instanceof DateClassifier)) {
				continue;
			}

			long result = abs(targetDay - ((DateClassifier)element).getDate().toDateTimeAtStartOfDay().getMillis());

			// If the new compareTo is closer than the previous one, we update the references.
			if(compareToResult < 0 || result < compareToResult) {
				compareToResult = result;
				lastPosition = i;
			}
		}

		// And we return the last position.
		return lastPosition;
	}

	/**
	 * Returns the absolute value of a long a.
	 *
	 * @param a The long.
	 *
	 * @return The absolute value of a.
	 */

	public static long abs(final long a) {
		return a < 0 ? -a : a;
	}

	/**
	 * Copies a file.
	 *
	 * @param source The source file.
	 * @param destination Its destination.
	 *
	 * @throws IOException If any I/O exception occurs.
	 */

	public static void copy(final File source, final File destination) throws IOException {
		if(!destination.exists() && !destination.createNewFile()) {
			throw new IOException("Can\'t create file !");
		}

		// A little copy function : we open an input and output stream and we copy each byte of the source file to the destination file.
		try(final InputStream inputStream = new FileInputStream(source)) {
			try(final OutputStream outputStream = new FileOutputStream(destination)) {
				final byte[] buffer = new byte[1024];
				int length;
				while((length = inputStream.read(buffer)) > 0) {
					outputStream.write(buffer, 0, length);
				}
			}
		}
	}

	/**
	 * Restarts the app.
	 *
	 * @param context The context.
	 */

	public static boolean restart(final Context context) {
		try {
			// Fetch the PackageManager so we can get the default launch activity.
			final PackageManager packageManager = context.getPackageManager();
			if(packageManager == null) {
				return false;
			}

			// Create the intent with the default start activity for your application.
			final Intent startActivity = packageManager.getLaunchIntentForPackage(context.getPackageName());
			if(startActivity == null) {
				return false;
			}
			startActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			// Create a pending intent so the application is restarted after System.exit(0) was called.
			final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, startActivity, PendingIntent.FLAG_CANCEL_CURRENT);

			// We use an AlarmManager to call this intent in 100ms.
			final AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);

			// We kill the application.
			System.exit(0);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}

		return true;
	}

}