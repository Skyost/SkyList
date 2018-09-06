package fr.skyost.skylist.task.adapter.classifier.date;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import fr.skyost.skylist.task.TodoTask;
import fr.skyost.skylist.task.adapter.classifier.Classifier;
import fr.skyost.skylist.task.adapter.classifier.period.PeriodClassifier;

/**
 * The ascending date classifier.
 */

public class AscendingDateClassifier extends DateClassifier {

	/**
	 * The preference value.
	 */

	public static final String PREFERENCE_VALUE = "5";

	/**
	 * Creates a new ascending date classifier instance.
	 *
	 * @param date The date.
	 */

	private AscendingDateClassifier(final LocalDate date) {
		super(date);
	}

	/**
	 * Returns the Classifier that corresponds to the specified task.
	 *
	 * @param task The task.
	 *
	 * @return The Classifier that corresponds to the specified task.
	 */

	public static AscendingDateClassifier get(final TodoTask task) {
		return new AscendingDateClassifier(task.getDate());
	}

	/**
	 * Returns the Classifier that corresponds to the current date.
	 *
	 * @return The Classifier that corresponds to the current date.
	 */

	public static AscendingDateClassifier getTodayClassifier() {
		return new AscendingDateClassifier(LocalDate.now());
	}

	@Override
	public int compareTo(
			@NonNull
			final Classifier classifier) {
		if(classifier instanceof PeriodClassifier) {
			return getDate().compareTo(((PeriodClassifier)classifier).getDate());
		}
		return -1;
	}

}