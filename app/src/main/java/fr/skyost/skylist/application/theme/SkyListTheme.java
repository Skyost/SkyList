package fr.skyost.skylist.application.theme;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Menu;
import android.view.View;
import android.view.Window;

import androidx.annotation.ColorRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import fr.skyost.skylist.activity.settings.SettingsActivity;

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
	 * Returns the menu icons color resource id.
	 *
	 * @return The menu icons color resource id (or -1 if disabled).
	 */

	@ColorRes
	public int getMenuIconsColor() {
		return -1;
	}

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
		// We apply the theme.
		activity.setTheme(getTheme());

		// If applied after the super.onCreate(...) we have to ensure the correct background color has been applied because it does not work all the time.
		// activity.getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(activity, getPrimaryColorDark()));

		// If styling is supported for the navigation bar and the status bar, then we can apply it !
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			final Window window = activity.getWindow();
			window.setNavigationBarColor(activity.getResources().getColor(getPrimaryColor()));
			window.setStatusBarColor(activity.getResources().getColor(getPrimaryColor()));

			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				final int flags = window.getDecorView().getSystemUiVisibility();
				window.getDecorView().setSystemUiVisibility(useNightMode() ? (flags & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) : (flags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));
			}
		}
	}

	/**
	 * Applies this theme to a menu.
	 *
	 * @param menu The menu.
	 */

	public void apply(final Context context, final Menu menu) {
		// We change the color of each menu drawable according to the current theme.
		if(getMenuIconsColor() == -1) {
			return;
		}

		// We iterate over each menu item and we apply our menu icons color.
		final int color = ContextCompat.getColor(context, getMenuIconsColor());
		final int n = menu.size();
		for(int i = 0; i < n; i++) {
			final Drawable drawable = menu.getItem(i).getIcon();
			if(drawable != null) {
				drawable.mutate();
				drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
			}
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