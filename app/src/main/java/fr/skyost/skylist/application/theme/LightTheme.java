package fr.skyost.skylist.application.theme;

import fr.skyost.skylist.R;

/**
 * The light theme.
 */

public class LightTheme extends SkyListTheme {

	/**
	 * The preference value.
	 */

	public static final String PREFERENCE_VALUE = "1";

	/**
	 * Creates a new light theme instance.
	 */

	LightTheme() {
		super();
	}

	@Override
	public int getTheme() {
		return R.style.AppTheme_Light;
	}

	@Override
	public int getPrimaryColor() {
		return R.color.light_primary;
	}

	@Override
	public int getPrimaryColorDark() {
		return R.color.light_primary_dark;
	}

	@Override
	public int getMenuIconsColor() {
		return R.color.light_text;
	}

	@Override
	public int getClassifierColor() {
		return R.color.light_classifier;
	}

	@Override
	public int getTodoTaskDescriptionColor() {
		return R.color.light_todo_task_description;
	}

	@Override
	public int getTodoTaskDateColor() {
		return R.color.light_todo_task_date;
	}

	@Override
	public int getTodoTaskSelectedBackgroundColor() {
		return R.color.light_todo_task_selected_background;
	}

	@Override
	public boolean useNightMode() {
		return false;
	}

}