package fr.skyost.skylist.task.adapter.classifier.alphabetical;

import android.support.annotation.NonNull;

import fr.skyost.skylist.task.TodoTask;
import fr.skyost.skylist.task.adapter.classifier.Classifier;

/**
 * The descending alphabetical classifier.
 */

public class DescendingAlphabeticalClassifier extends AlphabeticalClassifier {

	/**
	 * The preference value.
	 */

	public static final String PREFERENCE_VALUE = "3";

	/**
	 * Creates a new descending alphabetical classifier instance.
	 *
	 * @param letter The letter.
	 */

	private DescendingAlphabeticalClassifier(final String letter) {
		super(letter);
	}

	/**
	 * Returns the Classifier that corresponds to the specified task.
	 *
	 * @param task The task.
	 *
	 * @return The Classifier that corresponds to the specified task.
	 */

	public static AlphabeticalClassifier get(final TodoTask task) {
		final String description = task.getDescription();
		if(description.isEmpty()) {
			return new DescendingAlphabeticalClassifier(null);
		}

		final char letter = description.charAt(0);
		return new DescendingAlphabeticalClassifier(Character.isLetter(letter) ? String.valueOf(letter).toUpperCase() : null);
	}

	@Override
	public int compareTo(@NonNull final Classifier classifier) {
		if(getLetter() != null && classifier instanceof AlphabeticalClassifier) {
			return ((AlphabeticalClassifier)classifier).getLetter().compareTo(getLetter());
		}
		return 1;
	}

}