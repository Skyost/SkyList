package fr.skyost.skylist.task.adapter.classifier;

import android.content.res.Resources;
import android.support.annotation.StringRes;

/**
 * A Classifier that displays a String resource.
 */

public abstract class ResourceClassifier extends Classifier {

	/**
	 * The resource ID.
	 */

	private int resourceId;

	/**
	 * Creates a new resource classifier instance.
	 *
	 * @param resourceId The resource ID.
	 */

	public ResourceClassifier(@StringRes final int resourceId) {
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

	public void setResourceId(@StringRes final int resourceId) {
		this.resourceId = resourceId;
	}

	@Override
	public String getDisplayString(final Resources resources) {
		return resources.getString(resourceId);
	}

}