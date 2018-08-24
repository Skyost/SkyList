package fr.skyost.skylist.task.adapter.classifier.alphabetical;

import android.content.res.Resources;

import fr.skyost.skylist.R;
import fr.skyost.skylist.task.adapter.classifier.Classifier;

/**
 * A Classifier class that allows to sort tasks based on their first letter.
 */

public abstract class AlphabeticalClassifier extends Classifier {

	/**
	 * The character when the description doesn't start with a proper letter.
	 */

	static final char OTHER = 'a';

	/**
	 * The letter.
	 */

	private final char letter;

	/**
	 * Creates a new alphabetical classifier instance.
	 *
	 * @param letter The letter.
	 */

	AlphabeticalClassifier(final char letter) {
		this.letter = letter;
	}

	/**
	 * Returns the letter.
	 *
	 * @return The letter.
	 */

	public char getLetter() {
		return letter;
	}

	@Override
	public String getDisplayString(final Resources resources) {
		return letter == OTHER ? resources.getString(R.string.main_classifier_character_others) : String.valueOf(letter);
	}

}