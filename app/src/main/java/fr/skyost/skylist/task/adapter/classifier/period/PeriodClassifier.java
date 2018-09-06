package fr.skyost.skylist.task.adapter.classifier.period;

import android.content.res.Resources;
import android.support.annotation.StringRes;

import org.joda.time.LocalDate;

import fr.skyost.skylist.task.adapter.classifier.date.DateClassifier;

/**
 * A Classifier that allows to sort tasks based on their time period.
 */

public abstract class PeriodClassifier extends DateClassifier {

	/**
	 * The resource ID.
	 */

	private int resourceId;

	/**
	 * Creates a new period classifier instance.
	 *
	 * @param resourceId The resource ID of the display String.
	 * @param date The date.
	 */

	PeriodClassifier(
			@StringRes
			final int resourceId, final LocalDate date) {
		super(date);
		this.resourceId = resourceId;
	}

	/**
	 * Returns the resource ID.
	 *
	 * @return The resource ID.
	 */

	@StringRes
	public int getResourceId() {
		return resourceId;
	}

	/**
	 * Sets the resource ID.
	 *
	 * @param resourceId The resource ID.
	 */

	public void setResourceId(
			@StringRes
			final int resourceId) {
		this.resourceId = resourceId;
	}

	@Override
	public String getDisplayString(final Resources resources) {
		return resources.getString(resourceId);
	}

}