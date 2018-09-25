package fr.skyost.skylist.task.adapter.classifier.date;

import org.joda.time.LocalDate;

import androidx.annotation.NonNull;
import fr.skyost.skylist.task.TodoTask;
import fr.skyost.skylist.task.adapter.classifier.Classifier;

/**
 * The descending date classifier.
 */

public class DescendingDateClassifier extends DateClassifier {

	/**
	 * The preference value.
	 */

	public static final String PREFERENCE_VALUE = "6";

	/**
	 * Creates a new descending date classifier instance.
	 *
	 * @param date The date.
	 */

	private DescendingDateClassifier(final LocalDate date) {
		super(date);
	}

	/**
	 * Returns the Classifier that corresponds to the specified task.
	 *
	 * @param task The task.
	 *
	 * @return The Classifier that corresponds to the specified task.
	 */

	public static DescendingDateClassifier get(final TodoTask task) {
		return new DescendingDateClassifier(task.getDate());
	}

	/**
	 * Returns the Classifier that corresponds to the current date.
	 *
	 * @return The Classifier that corresponds to the current date.
	 */

	public static DescendingDateClassifier getTodayClassifier() {
		return new DescendingDateClassifier(LocalDate.now());
	}

	@Override
	public int compareTo(
			@NonNull
			final Classifier classifier) {
		if(classifier instanceof DateClassifier) {
			return ((DateClassifier)classifier).getDate().compareTo(getDate());
		}
		return -1;
	}

}