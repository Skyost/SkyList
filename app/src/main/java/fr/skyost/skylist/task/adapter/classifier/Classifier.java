package fr.skyost.skylist.task.adapter.classifier;

import android.content.Context;
import android.content.res.Resources;

import fr.skyost.skylist.activity.settings.SettingsActivity;
import fr.skyost.skylist.task.TodoTask;
import fr.skyost.skylist.task.adapter.TodoListAdapterItem;
import fr.skyost.skylist.task.adapter.classifier.alphabetical.AscendingAlphabeticalClassifier;
import fr.skyost.skylist.task.adapter.classifier.alphabetical.DescendingAlphabeticalClassifier;
import fr.skyost.skylist.task.adapter.classifier.date.AscendingDateClassifier;
import fr.skyost.skylist.task.adapter.classifier.date.DescendingDateClassifier;
import fr.skyost.skylist.task.adapter.classifier.period.AscendingPeriodClassifier;
import fr.skyost.skylist.task.adapter.classifier.period.DescendingPeriodClassifier;
import fr.skyost.skylist.util.Function;

/**
 * Represents a Classifier that allows to sort tasks based on custom criteria.
 */

public abstract class Classifier implements TodoListAdapterItem, Comparable<Classifier> {

	/**
	 * Type of this item in the adapter.
	 */

	public static final int ADAPTER_ITEM_TYPE = 1;

	/**
	 * Returns the String that allows to display this Classifier.
	 *
	 * @param resources The Resources instance.
	 *
	 * @return The String that allows to display this Classifier.
	 */

	public abstract String getDisplayString(final Resources resources);

	@Override
	public int getType() {
		return ADAPTER_ITEM_TYPE;
	}

	@Override
	public boolean equals(final Object object) {
		if(object instanceof Classifier) {
			return compareTo((Classifier)object) == 0;
		}

		return super.equals(object);
	}

	/**
	 * Returns the function that allows to get the Classifier that corresponds to a given task (based on SharedPreferences).
	 *
	 * @param context The Context.
	 *
	 * @return The function that allows to get the Classifier that corresponds to a given task.
	 */

	public static Function<TodoTask, Classifier> getClassifierFunction(final Context context) {
		// We switch over the preference value and we return corresponding function.
		switch(context.getSharedPreferences(SettingsActivity.APP_PREFERENCES, Context.MODE_PRIVATE).getString("list_order", DescendingPeriodClassifier.PREFERENCE_VALUE)) {
		case AscendingPeriodClassifier.PREFERENCE_VALUE:
			return AscendingPeriodClassifier::get;
		case AscendingDateClassifier.PREFERENCE_VALUE:
			return AscendingDateClassifier::get;
		case DescendingDateClassifier.PREFERENCE_VALUE:
			return DescendingDateClassifier::get;
		case AscendingAlphabeticalClassifier.PREFERENCE_VALUE:
			return AscendingAlphabeticalClassifier::get;
		case DescendingAlphabeticalClassifier.PREFERENCE_VALUE:
			return DescendingAlphabeticalClassifier::get;
		case ColorClassifier.PREFERENCE_VALUE:
			ColorClassifier.init(context.getResources());
			return ColorClassifier::get;
		default:
			return DescendingPeriodClassifier::get;
		}
	}

}