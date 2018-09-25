package fr.skyost.skylist.task.adapter.classifier;

import android.content.res.Resources;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import fr.skyost.skylist.R;
import fr.skyost.skylist.task.TodoTask;

/**
 * A Classifier that allows sort tasks by their color.
 */

public class ColorClassifier extends Classifier {

	/**
	 * The preference value.
	 */

	public static final String PREFERENCE_VALUE = "4";

	/**
	 * Cached classifiers.
	 */

	private static final SparseArray<ColorClassifier> CLASSIFIERS = new SparseArray<>();

	/**
	 * The color name.
	 */

	private final String color;

	/**
	 * Creates a new color classifier instance.
	 *
	 * @param color The color.
	 */

	private ColorClassifier(final String color) {
		this.color = color;
	}

	/**
	 * Returns the Classifier that corresponds to the specified task.
	 *
	 * @param task The task.
	 *
	 * @return The Classifier that corresponds to the specified task.
	 */

	public static ColorClassifier get(final TodoTask task) {
		return CLASSIFIERS.get(task.getColor());
	}

	/**
	 * Initializes the ColorClassifier class.
	 *
	 * @param resources The Resources object.
	 */

	public static void init(final Resources resources) {
		// We keeps a reference to the colors value and name, then we put everything in our SparseArray.
		final int[] colors = resources.getIntArray(R.array.todo_task_available_colors);
		final String[] names = resources.getStringArray(R.array.main_classifier_colors);
		for(int i = 0; i < colors.length; i++) {
			CLASSIFIERS.put(colors[i], new ColorClassifier(names[i]));
		}
	}

	/**
	 * Returns the color name.
	 *
	 * @return The color name.
	 */

	public String getColor() {
		return color;
	}

	@Override
	public String getDisplayString(final Resources resources) {
		return color;
	}

	@Override
	public int compareTo(@NonNull final Classifier classifier) {
		if(classifier instanceof ColorClassifier) {
			return color.compareTo(((ColorClassifier)classifier).color);
		}
		return -1;
	}

}