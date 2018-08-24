package fr.skyost.skylist.application.theme;

import fr.skyost.skylist.R;

/**
 * The yellow theme.
 */

public class YellowTheme extends SkyListTheme {

	/**
	 * The preference value.
	 */

	public static final String PREFERENCE_VALUE = "3";

	/**
	 * Creates a new yellow theme instance.
	 */

	YellowTheme() {
		super();
	}

	@Override
	public int getTheme() {
		return R.style.AppTheme_Yellow;
	}

	@Override
	public int getPrimaryColor() {
		return R.color.yellow_primary;
	}

	@Override
	public int getPrimaryColorDark() {
		return R.color.yellow_primary_dark;
	}

	@Override
	public int getClassifierColor() {
		return R.color.yellow_classifier;
	}

	@Override
	public int getTodoTaskDescriptionColor() {
		return R.color.yellow_todo_task_description;
	}

	@Override
	public int getTodoTaskDateColor() {
		return R.color.yellow_todo_task_date;
	}

	@Override
	public int getTodoTaskSelectedBackgroundColor() {
		return R.color.yellow_todo_task_selected_background;
	}


	@Override
	public boolean useNightMode() {
		return false;
	}

}