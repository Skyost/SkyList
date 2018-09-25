package fr.skyost.skylist.task.adapter.classifier.alphabetical;

import androidx.annotation.NonNull;
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

	private DescendingAlphabeticalClassifier(final char letter) {
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
			return new DescendingAlphabeticalClassifier(OTHER);
		}

		final char letter = description.charAt(0);
		return new DescendingAlphabeticalClassifier(Character.isLetter(letter) ? Character.toUpperCase(letter) : OTHER);
	}

	@Override
	public int compareTo(@NonNull final Classifier classifier) {
		if(classifier instanceof AlphabeticalClassifier) {
			return Character.compare(((AlphabeticalClassifier)classifier).getLetter(), getLetter());
		}
		return -1;
	}

}