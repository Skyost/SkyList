package fr.skyost.skylist.task.adapter.classifier.period;

import org.joda.time.LocalDate;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import fr.skyost.skylist.R;
import fr.skyost.skylist.task.TodoTask;
import fr.skyost.skylist.task.adapter.classifier.Classifier;
import fr.skyost.skylist.task.adapter.classifier.date.DateClassifier;

/**
 * The descending period classifier.
 */

public class DescendingPeriodClassifier extends PeriodClassifier {

	/**
	 * The preference value.
	 */

	public static final String PREFERENCE_VALUE = "1";

	/**
	 * Upcoming Classifier.
	 */

	public static final DescendingPeriodClassifier UPCOMING = new DescendingPeriodClassifier(R.string.main_classifier_dateRange_upcoming, LocalDate.now().plusDays(2));

	/**
	 * Tomorrow Classifier.
	 */

	public static final DescendingPeriodClassifier TOMORROW = new DescendingPeriodClassifier(R.string.main_classifier_dateRange_tomorrow, LocalDate.now().plusDays(1));

	/**
	 * Today Classifier.
	 */

	public static final DescendingPeriodClassifier TODAY = new DescendingPeriodClassifier(R.string.main_classifier_dateRange_today, LocalDate.now());

	/**
	 * Yesterday Classifier.
	 */

	public static final DescendingPeriodClassifier YESTERDAY = new DescendingPeriodClassifier(R.string.main_classifier_dateRange_yesterday, LocalDate.now().minusDays(1));

	/**
	 * Past Classifier.
	 */

	public static final DescendingPeriodClassifier PAST = new DescendingPeriodClassifier(R.string.main_classifier_dateRange_past, LocalDate.now().minusDays(2));

	/**
	 * Creates a new descending period classifier instance.
	 *
	 * @param resourceId The display String resource ID.
	 * @param date The date.
	 */

	private DescendingPeriodClassifier(
			@StringRes
			final int resourceId, final LocalDate date) {
		super(resourceId, date);
	}

	/**
	 * Returns the Classifier that corresponds to the specified task.
	 *
	 * @param task The task.
	 *
	 * @return The Classifier that corresponds to the specified task.
	 */

	public static DescendingPeriodClassifier get(final TodoTask task) {
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

	/**
	 * Returns the Classifier that corresponds to the current date.
	 *
	 * @return The Classifier that corresponds to the current date.
	 */

	public static DescendingPeriodClassifier getTodayClassifier() {
		return TODAY;
	}

	@Override
	public int compareTo(@NonNull final Classifier classifier) {
		if(classifier instanceof DateClassifier) {
			return ((DateClassifier)classifier).getDate().compareTo(getDate());
		}
		return -1;
	}

}