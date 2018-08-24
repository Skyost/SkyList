package fr.skyost.skylist.task.adapter.classifier.date;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import org.joda.time.LocalDate;

import fr.skyost.skylist.R;
import fr.skyost.skylist.task.TodoTask;
import fr.skyost.skylist.task.adapter.classifier.Classifier;

/**
 * The descending date range classifier.
 */

public class DescendingDateRangeClassifier extends DateRangeClassifier {

	/**
	 * The preference value.
	 */

	public static final String PREFERENCE_VALUE = "1";

	/**
	 * Upcoming Classifier.
	 */

	public static final DescendingDateRangeClassifier UPCOMING = new DescendingDateRangeClassifier(R.string.main_classifier_daterange_upcoming, LocalDate.now().plusDays(2));

	/**
	 * Tomorrow Classifier.
	 */

	public static final DescendingDateRangeClassifier TOMORROW = new DescendingDateRangeClassifier(R.string.main_classifier_daterange_tomorrow, LocalDate.now().plusDays(1));

	/**
	 * Today Classifier.
	 */

	public static final DescendingDateRangeClassifier TODAY = new DescendingDateRangeClassifier(R.string.main_classifier_daterange_today, LocalDate.now());

	/**
	 * Yesterday Classifier.
	 */

	public static final DescendingDateRangeClassifier YESTERDAY = new DescendingDateRangeClassifier(R.string.main_classifier_daterange_yesterday, LocalDate.now().minusDays(1));

	/**
	 * Past Classifier.
	 */

	public static final DescendingDateRangeClassifier PAST = new DescendingDateRangeClassifier(R.string.main_classifier_daterange_past, LocalDate.now().minusDays(2));

	/**
	 * Creates a new descending date range classifier instance.
	 *
	 * @param resourceId The display String resource ID.
	 * @param date The date.
	 */

	private DescendingDateRangeClassifier(@StringRes final int resourceId, final LocalDate date) {
		super(resourceId, date);
	}

	/**
	 * Returns the Classifier that corresponds to the specified task.
	 *
	 * @param task The task.
	 *
	 * @return The Classifier that corresponds to the specified task.
	 */

	public static DescendingDateRangeClassifier get(final TodoTask task) {
		// The following code is pretty self-explanatory thanks to Joda-Time API.
		final LocalDate date = task.getDate();

		if(date.equals(TOMORROW.getDate())) {
			return TOMORROW;
		}

		if(date.isAfter(TOMORROW.getDate())) {
			return UPCOMING;
		}

		if(date.equals(YESTERDAY.getDate())) {
			return YESTERDAY;
		}

		if(date.isBefore(YESTERDAY.getDate())) {
			return PAST;
		}

		return TODAY;
	}

	@Override
	public int compareTo(@NonNull final Classifier classifier) {
		if(classifier instanceof DateRangeClassifier) {
			return ((DateRangeClassifier)classifier).getDate().compareTo(getDate());
		}
		return -1;
	}

}