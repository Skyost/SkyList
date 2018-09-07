package fr.skyost.skylist.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.content.res.AppCompatResources;
import android.view.MenuItem;

import com.kobakei.ratethisapp.RateThisApp;

import fr.skyost.skylist.R;
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

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// We only have one fragment, so let's display it !
		getFragmentManager().beginTransaction().replace(android.R.id.content, new AppPreferencesFragment()).commit();

		// It's time to change the home icon drawable.
		final ActionBar actionBar = getSupportActionBar();
		final Drawable up = AppCompatResources.getDrawable(this, R.drawable.settings_menu_home);
		if(actionBar == null || up == null) {
			return;
		}

		final int color = getSkyListTheme().getMenuIconsColor();
		if(color != -1) {
			up.setColorFilter(ContextCompat.getColor(this, color), PorterDuff.Mode.SRC_ATOP);
		}
		actionBar.setHomeAsUpIndicator(up);
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

	/**
	 * The preferences fragment.
	 */

	public static class AppPreferencesFragment extends PreferenceFragment {

		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// We set the preference file and we add the preferences from the specified resource file.
			getPreferenceManager().setSharedPreferencesName(APP_PREFERENCES);
			addPreferencesFromResource(R.xml.app_preferences);

			// Then we add all the required listeners to the preferences.
			findPreference("app_theme").setOnPreferenceChangeListener(((preference, newValue) -> openRestartApplicationDialogIfNeeded("show_app_theme_dialog")));
			findPreference("app_notification").setOnPreferenceChangeListener((preference, newValue) -> {
				if((Boolean)newValue) {
					NotificationHandler.scheduleNextNotification(this.getActivity());
				}
				return true;
			});

			findPreference("list_order").setOnPreferenceChangeListener((preference, newValue) -> {
				toggleScrollToTodayPreference(newValue);
				return openRestartApplicationDialogIfNeeded("show_list_order_dialog");
			});
			findPreference("about_app").setOnPreferenceClickListener(preference -> {
				RateThisApp.showRateDialog(getActivity());
				return true;
			});
			findPreference("about_skyost").setOnPreferenceClickListener((this::openDescriptionInBrowser));
			findPreference("about_github").setOnPreferenceClickListener((this::openDescriptionInBrowser));
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

}
