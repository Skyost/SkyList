package fr.skyost.skylist.task.adapter.classifier.date;

import android.content.Context;
import android.content.res.Resources;

import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;

import fr.skyost.skylist.R;
import fr.skyost.skylist.activity.SettingsActivity;
import fr.skyost.skylist.task.adapter.classifier.Classifier;
import fr.skyost.skylist.task.adapter.classifier.period.AscendingPeriodClassifier;
import fr.skyost.skylist.task.adapter.classifier.period.DescendingPeriodClassifier;

/**
 * A Classifier that allows to sort tasks based on their date.
 */

public abstract class DateClassifier extends Classifier {

	/**
	 * The date.
	 */

	private final LocalDate date;

	/**
	 * Returns the Classifier that corresponds to the current date.
	 *
	 * @return The Classifier that corresponds to the current date.
	 */

	public static DateClassifier getTodayClassifier(final Context context) {
		switch(context.getSharedPreferences(SettingsActivity.APP_PREFERENCES, Context.MODE_PRIVATE).getString("list_order", DescendingPeriodClassifier.PREFERENCE_VALUE)) {
		case AscendingPeriodClassifier.PREFERENCE_VALUE:
			return AscendingPeriodClassifier.getTodayClassifier();
		case DescendingPeriodClassifier.PREFERENCE_VALUE:
			return DescendingPeriodClassifier.getTodayClassifier();
		case AscendingDateClassifier.PREFERENCE_VALUE:
			return AscendingDateClassifier.getTodayClassifier();
		case DescendingDateClassifier.PREFERENCE_VALUE:
			return DescendingDateClassifier.getTodayClassifier();
		default:
			return null;
		}
	}

	/**
	 * Creates a new date classifier instance.
	 *
	 * @param date The date.
	 */

	protected DateClassifier(final LocalDate date) {
		this.date = date;
	}

	/**
	 * Returns the date.
	 *
	 * @return The date.
	 */

	public LocalDate getDate() {
		return date;
	}

	@Override
	public String getDisplayString(final Resources resources) {
		final String display = SimpleDateFormat.getDateInstance().format(date.toDate());
		if(!date.isEqual(LocalDate.now())) {
			return display;
		}

		return display + " (" + resources.getString(R.string.main_classifier_dateRange_today) + ")";
	}

}