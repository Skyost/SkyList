package fr.skyost.skylist.application.theme;

import fr.skyost.skylist.R;

/**
 * The pink theme.
 */

public class PinkTheme extends SkyListTheme {

	/**
	 * The preference value.
	 */

	public static final String PREFERENCE_VALUE = "2";

	/**
	 * Creates a new pink theme instance.
	 */

	PinkTheme() {
		super();
	}

	@Override
	public int getTheme() {
		return R.style.AppTheme_Pink;
	}

	@Override
	public int getPrimaryColor() {
		return R.color.pink_primary;
	}

	@Override
	public int getPrimaryColorDark() {
		return R.color.pink_primary_dark;
	}

	@Override
	public int getClassifierColor() {
		return R.color.pink_classifier;
	}

	@Override
	public int getTodoTaskDescriptionColor() {
		return R.color.pink_todo_task_description;
	}

	@Override
	public int getTodoTaskDateColor() {
		return R.color.pink_todo_task_date;
	}

	@Override
	public int getTodoTaskSelectedBackgroundColor() {
		return R.color.pink_todo_task_selected_background;
	}

}