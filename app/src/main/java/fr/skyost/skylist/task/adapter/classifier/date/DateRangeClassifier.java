package fr.skyost.skylist.task.adapter.classifier.date;

import android.support.annotation.StringRes;

import org.joda.time.LocalDate;

import fr.skyost.skylist.task.adapter.classifier.ResourceClassifier;

/**
 * A Classifier that allows to sort tasks based on their date.
 */

public abstract class DateRangeClassifier extends ResourceClassifier {

	/**
	 * The date.
	 */

	private final LocalDate date;

	/**
	 * Creates a new date range classifier instance.
	 *
	 * @param resourceId The resource ID of the display String.
	 * @param date The date.
	 */

	DateRangeClassifier(@StringRes final int resourceId, final LocalDate date) {
		super(resourceId);
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

}