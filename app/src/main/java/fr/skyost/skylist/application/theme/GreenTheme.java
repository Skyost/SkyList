package fr.skyost.skylist.application.theme;

import fr.skyost.skylist.R;

/**
 * The green theme.
 */

public class GreenTheme extends SkyListTheme {

	/**
	 * The preference value.
	 */

	public static final String PREFERENCE_VALUE = "4";

	/**
	 * Creates a new green theme instance.
	 */

	GreenTheme() {
		super();
	}

	@Override
	public int getTheme() {
		return R.style.AppTheme_Green;
	}

	@Override
	public int getPrimaryColor() {
		return R.color.green_primary;
	}

	@Override
	public int getPrimaryColorDark() {
		return R.color.green_primary_dark;
	}

	@Override
	public int getClassifierColor() {
		return R.color.green_classifier;
	}

	@Override
	public int getTodoTaskDescriptionColor() {
		return R.color.green_todo_task_description;
	}

	@Override
	public int getTodoTaskDateColor() {
		return R.color.green_todo_task_date;
	}

	@Override
	public int getTodoTaskSelectedBackgroundColor() {
		return R.color.green_todo_task_selected_background;
	}

}