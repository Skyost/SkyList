package fr.skyost.skylist.activity.settings;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;

import com.github.jksiezni.permissive.Permissive;
import com.kobakei.ratethisapp.RateThisApp;

import java.io.File;

import fr.skyost.skylist.R;
import fr.skyost.skylist.activity.SkyListActivity;
import fr.skyost.skylist.application.SkyListApplication;
import fr.skyost.skylist.notification.NotificationHandler;
import fr.skyost.skylist.task.adapter.classifier.date.AscendingDateClassifier;
import fr.skyost.skylist.task.adapter.classifier.date.DescendingDateClassifier;
import fr.skyost.skylist.task.adapter.classifier.period.AscendingPeriodClassifier;
import fr.skyost.skylist.task.adapter.classifier.period.DescendingPeriodClassifier;

/**
 * The activity that allows to configure the application.
 */

public class SettingsActivity extends SkyListActivity {

	/**
	 * The app SharedPreferences file.
	 */

	public static final String APP_PREFERENCES = "app_preferences";

	/**
	 * The need restart bundle key.
	 */

	public static String BUNDLE_NEED_RESTART = "need-restart";

	/**
	 * Whether a restart of the main activity is needed.
	 */

	private boolean needRestart = false;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// We have to restore the fields.
		if(savedInstanceState != null) {
			needRestart = savedInstanceState.getBoolean(BUNDLE_NEED_RESTART, false);
		}

		// We only have one fragment, so let's display it !
		getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new AppPreferencesFragment()).commit();

		// It's time to change the home icon drawable.
		final ActionBar actionBar = getSupportActionBar();
		final Drawable up = AppCompatResources.getDrawable(this, R.drawable.settings_menu_home);
		if(actionBar == null || up == null) {
			return;
		}

		// Allows to color the home icon drawable.
		final int color = getSkyListTheme().getMenuIconsColor();
		if(color != -1) {
			up.setColorFilter(ContextCompat.getColor(this, color), PorterDuff.Mode.SRC_ATOP);
		}
		actionBar.setHomeAsUpIndicator(up);
	}

	@Override
	protected void onSaveInstanceState(final Bundle outState) {
		outState.putBoolean(BUNDLE_NEED_RESTART, needRestart);

		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			// When the user press the home button (in the Action Bar), we want to handle it has a back key press.
			onBackPressed();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		final Intent intent = new Intent();
		intent.putExtra(BUNDLE_NEED_RESTART, needRestart);
		setResult(Activity.RESULT_OK, intent);

		super.onBackPressed();
	}

	/**
	 * The preferences fragment.
	 */

	public static class AppPreferencesFragment extends PreferenceFragmentCompat {

		@Override
		public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
			final Activity activity = getActivity();
			if(activity == null) {
				return;
			}

			// We set the preference file and we add the preferences from the specified resource file.
			getPreferenceManager().setSharedPreferencesName(APP_PREFERENCES);
			addPreferencesFromResource(R.xml.app_preferences);

			// Then we add all the required listeners to the preferences.
			findPreference("app_theme").setOnPreferenceChangeListener(((preference, newValue) -> openRestartApplicationDialogIfNeeded("show_app_theme_dialog")));
			findPreference("app_notificationMode").setOnPreferenceChangeListener((preference, newValue) -> {
				if(!newValue.toString().equals("2")) {
					NotificationHandler.scheduleNextNotification(activity);
				}
				return true;
			});

			findPreference("list_order").setOnPreferenceChangeListener((preference, newValue) -> {
				toggleScrollToTodayPreference(newValue);
				return openRestartApplicationDialogIfNeeded("show_list_order_dialog");
			});

			findPreference("backup_backup").setOnPreferenceClickListener(preference -> backupDatabase());
			findPreference("backup_restore").setOnPreferenceClickListener(preference -> restoreDatabase());

			findPreference("about_app").setOnPreferenceClickListener(preference -> {
				RateThisApp.showRateDialog(activity);
				return true;
			});
			findPreference("about_skyost").setOnPreferenceClickListener(this::openDescriptionInBrowser);
			findPreference("about_github").setOnPreferenceClickListener(this::openDescriptionInBrowser);
		}

		/**
		 * Toggles the "scroll to today" preference according to the provided new value.
		 *
		 * @param newValue The new value.
		 */

		private void toggleScrollToTodayPreference(final Object newValue) {
			// We put the category and its preference into a variable.
			final PreferenceCategory category = (PreferenceCategory)findPreference("settings_category_list");
			final Preference scrollToToday = findPreference("list_scrollToToday");

			// We check if the user has enabled a date classifier.
			if(newValue.equals(AscendingPeriodClassifier.PREFERENCE_VALUE) || newValue.equals(DescendingPeriodClassifier.PREFERENCE_VALUE) || newValue.equals(AscendingDateClassifier.PREFERENCE_VALUE) || newValue.equals(DescendingDateClassifier.PREFERENCE_VALUE)) {
				// If there are already two preferences in the category then it's all good !
				if(category.getPreferenceCount() == 2) {
					return;
				}

				// Otherwise we can add our preference.
				category.addPreference(scrollToToday);
				return;
			}

			// Same here if there is only one preference, we can stop.
			if(category.getPreferenceCount() == 1) {
				return;
			}

			// Otherwise we have to remove our preference.
			category.removePreference(scrollToToday);
		}

		/**
		 * Opens the "You need to restart the application to see the changes" dialog if needed.
		 *
		 * @param preferenceKey The preference key that allows to not open the dialog in the future.
		 *
		 * @return Always true.
		 */

		private boolean openRestartApplicationDialogIfNeeded(final String preferenceKey) {
			// We ensure that we can open the dialog.
			final SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
			if(!preferences.getBoolean(preferenceKey, true)) {
				return true;
			}

			// If yes, we open it.
			new AlertDialog.Builder(getActivity())
					.setTitle(R.string.settings_list_order_warning_title)
					.setMessage(R.string.settings_list_order_warning_message)
					.setPositiveButton(android.R.string.ok, null)
					.setNeutralButton(R.string.settings_list_order_warning_button_neutral, (dialog, choice) -> {
						final SharedPreferences.Editor editor = preferences.edit();
						editor.putBoolean(preferenceKey, false);
						editor.apply();
					})
					.show();
			return true;
		}

		/**
		 * Backups the application database.
		 *
		 * @return Always true.
		 */

		private boolean backupDatabase() {
			// We get the activity.
			final Activity activity = getActivity();
			if(activity == null) {
				return true;
			}

			// And we try to launch the save database task.
			new Permissive.Request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
					.whenPermissionsGranted(permissions -> new SaveDatabaseTask().execute((SkyListApplication)activity.getApplication()))
					.whenPermissionsRefused(permissions -> openMissingPermissionDialog(activity))
					.execute(activity);

			return true;
		}

		/**
		 * Backups the application database.
		 *
		 * @return Always true.
		 */

		private boolean restoreDatabase() {
			// We get the activity.
			final Activity activity = getActivity();
			if(activity == null) {
				return true;
			}

			// And we try to launch the load database task.
			new Permissive.Request(Manifest.permission.READ_EXTERNAL_STORAGE)
					.whenPermissionsGranted(permissions -> {
						// If there is no backup directory, we can exit right away.
						final File sourceDirectory = SaveDatabaseTask.getBackupsDirectory();
						if(!sourceDirectory.exists()) {
							Toast.makeText(activity, R.string.toast_nobackup, Toast.LENGTH_LONG).show();
							return;
						}

						// Otherwise, we can show a dialog with a list of backups.
						final String[] backups = sourceDirectory.list();
						new AlertDialog.Builder(activity)
								.setItems(backups, (dialog, which) -> new LoadDatabaseTask(new File(sourceDirectory, backups[which])).execute((SettingsActivity)getActivity()))
								.setNegativeButton(android.R.string.cancel, null)
								.show();

					})
					.whenPermissionsRefused(permissions -> openMissingPermissionDialog(activity))
					.execute(activity);

			return true;
		}

		/**
		 * Opens a dialog that says that the application doesn't have sufficient permissions.
		 *
		 * @param context The context.
		 */

		private void openMissingPermissionDialog(final Context context) {
			new AlertDialog.Builder(context)
					.setTitle("Missing permission")
					.setMessage("We need to have access to your storage to backup / restore your tasks !")
					.setPositiveButton("Retry", (dialog, choice) -> backupDatabase())
					.setNegativeButton(android.R.string.cancel, null)
					.show();
		}

		/**
		 * Opens the URL (located in the specified preference description) in the browser.
		 *
		 * @param preference The Preference object.
		 *
		 * @return Always true.
		 */

		private boolean openDescriptionInBrowser(final Preference preference) {
			// We create the Intent that allows to open the browser to a specified URL, then we start it.
			final Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(preference.getSummary().toString()));
			startActivity(intent);
			return true;
		}

	}

	/**
	 * Returns whether the main activity needs to restart.
	 *
	 * @return Whether the main activity needs to restart.
	 */

	public boolean needRestart() {
		return needRestart;
	}

	/**
	 * Sets whether the main activity needs to restart.
	 *
	 * @param needRestart Whether the main activity needs to restart.
	 */

	public void setNeedRestart(final boolean needRestart) {
		this.needRestart = needRestart;
	}

}
