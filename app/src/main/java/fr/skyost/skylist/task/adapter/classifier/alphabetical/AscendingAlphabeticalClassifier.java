package fr.skyost.skylist.task.adapter.classifier.alphabetical;

import androidx.annotation.NonNull;
import fr.skyost.skylist.task.TodoTask;
import fr.skyost.skylist.task.adapter.classifier.Classifier;

/**
 * The ascending alphabetical classifier.
 */

public class AscendingAlphabeticalClassifier extends AlphabeticalClassifier {

	/**
	 * The preference value.
	 */

	public static final String PREFERENCE_VALUE = "2";

	/**
	 * Creates a new ascending alphabetical classifier instance.
	 *
	 * @param letter The letter.
	 */

	private AscendingAlphabeticalClassifier(final char letter) {
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
			return new AscendingAlphabeticalClassifier(OTHER);
		}

		final char letter = description.charAt(0);
		return new AscendingAlphabeticalClassifier(Character.isLetter(letter) ? Character.toUpperCase(letter) : OTHER);
	}

	@Override
	public int compareTo(@NonNull final Classifier classifier) {
		if(classifier instanceof AlphabeticalClassifier) {
			return Character.compare(getLetter(), ((AlphabeticalClassifier)classifier).getLetter());
		}
		return -1;
	}

}