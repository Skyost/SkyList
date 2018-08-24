package fr.skyost.skylist.task.adapter.classifier.alphabetical;

import android.content.res.Resources;

import fr.skyost.skylist.R;
import fr.skyost.skylist.task.adapter.classifier.Classifier;

/**
 * A Classifier class that allows to sort tasks based on their first letter.
 * TODO: Test with a special character.
 */

public abstract class AlphabeticalClassifier extends Classifier {

	/**
	 * The letter.
	 */

	private final String letter;

	/**
	 * Creates a new alphabetical classifier instance.
	 *
	 * @param letter The letter.
	 */

	AlphabeticalClassifier(final String letter) {
		this.letter = letter;
	}

	/**
	 * Returns the letter.
	 *
	 * @return The letter.
	 */

	public String getLetter() {
		return letter;
	}

	@Override
	public String getDisplayString(final Resources resources) {
		return letter == null ? resources.getString(R.string.main_classifier_character_others) : letter;
	}

}