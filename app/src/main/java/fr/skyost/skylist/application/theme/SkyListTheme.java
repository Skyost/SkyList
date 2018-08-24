package fr.skyost.skylist.application.theme;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatDelegate;

import fr.skyost.skylist.activity.SettingsActivity;

/**
 * Represents a SkyList theme that allows to theme the application.
 */

public abstract class SkyListTheme {

	/**
	 * Creates a new theme instance.
	 */

	SkyListTheme() {
		AppCompatDelegate.setDefaultNightMode(useNightMode() ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
	}

	/**
	 * Returns the theme resource id.
	 *
	 * @return The theme resource id.
	 */

	@StyleRes
	public abstract int getTheme();

	/**
	 * Returns the primary color resource id.
	 *
	 * @return The primary color resource id.
	 */

	@ColorRes
	public abstract int getPrimaryColor();

	/**
	 * Returns the darkened primary color resource id.
	 *
	 * @return The darkened primary color resource id.
	 */

	@ColorRes
	public abstract int getPrimaryColorDark();

	/**
	 * Returns the classifier color resource id.
	 *
	 * @return The classifier color resource id.
	 */

	@ColorRes
	public abstract int getClassifierColor();

	/**
	 * Returns the to-do task description resource id.
	 *
	 * @return The to-do task description color resource id.
	 */

	@ColorRes
	public abstract int getTodoTaskDescriptionColor();

	/**
	 * Returns the to-do task date color resource id.
	 *
	 * @return The to-do task date color resource id.
	 */

	@ColorRes
	public abstract int getTodoTaskDateColor();

	/**
	 * Returns the selected to-do task background color resource id.
	 *
	 * @return The selected to-do task background color resource id.
	 */

	@ColorRes
	public abstract int getTodoTaskSelectedBackgroundColor();

	/**
	 * Returns whether this theme should use the night mode.
	 *
	 * @return Whether this theme should use the night mode.
	 */

	public boolean useNightMode() {
		return true;
	}

	/**
	 * Applies this theme to an activity.
	 *
	 * @param activity The activity.
	 */

	public void apply(final Activity activity) {
		activity.setTheme(getTheme());

		if(Build.VERSION.SDK_INT >= 21) {
			activity.getWindow().setNavigationBarColor(activity.getResources().getColor(getPrimaryColor()));
			activity.getWindow().setStatusBarColor(activity.getResources().getColor(getPrimaryColor()));
		}
	}

	/**
	 * Returns the theme corresponding to the Context's SharedPreferences.
	 *
	 * @param context The Context.
	 *
	 * @return The corresponding theme.
	 */

	public static SkyListTheme load(final Context context) {
		final String value = context.getSharedPreferences(SettingsActivity.APP_PREFERENCES, Context.MODE_PRIVATE).getString("app_theme", DarkTheme.PREFERENCE_VALUE);
		switch(value) {
		case LightTheme.PREFERENCE_VALUE:
			return new LightTheme();
		case PinkTheme.PREFERENCE_VALUE:
			return new PinkTheme();
		case YellowTheme.PREFERENCE_VALUE:
			return new YellowTheme();
		case GreenTheme.PREFERENCE_VALUE:
			return new GreenTheme();
		default:
			return new DarkTheme();
		}
	}

}