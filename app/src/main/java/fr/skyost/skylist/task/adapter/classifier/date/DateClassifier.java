package fr.skyost.skylist.task.adapter.classifier.date;

import android.content.res.Resources;

import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;

import fr.skyost.skylist.task.adapter.classifier.Classifier;

/**
 * A Classifier that allows to sort tasks based on their date.
 */

public abstract class DateClassifier extends Classifier {

	/**
	 * The date.
	 */

	private final LocalDate date;

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
		return SimpleDateFormat.getDateInstance().format(date.toDate());
	}

}