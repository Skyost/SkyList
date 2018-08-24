package fr.skyost.skylist.application.theme;

import fr.skyost.skylist.R;

/**
 * The dark theme.
 */

public class DarkTheme extends SkyListTheme {

	/**
	 * The preference value.
	 */

	public static final String PREFERENCE_VALUE = "0";

	/**
	 * Creates a new dark theme instance.
	 */

	DarkTheme() {
		super();
	}

	@Override
	public int getTheme() {
		return R.style.AppTheme;
	}

	@Override
	public int getPrimaryColor() {
		return R.color.dark_primary;
	}

	@Override
	public int getPrimaryColorDark() {
		return R.color.dark_primary_dark;
	}

	@Override
	public int getClassifierColor() {
		return R.color.dark_classifier;
	}

	@Override
	public int getTodoTaskDescriptionColor() {
		return R.color.dark_todo_task_description;
	}

	@Override
	public int getTodoTaskDateColor() {
		return R.color.dark_todo_task_date;
	}

	@Override
	public int getTodoTaskSelectedBackgroundColor() {
		return R.color.dark_todo_task_selected_background;
	}

}