package fr.skyost.skylist.util;

import java.util.List;

import fr.skyost.skylist.task.adapter.TodoListAdapterItem;
import fr.skyost.skylist.task.adapter.classifier.date.DateClassifier;

/**
 * This class contains some useful methods.
 */

public class Utils {

	/**
	 * Returns the closest element in the list using compareTo(...).
	 *
	 * @param target The target element.
	 * @param list The list.
	 *
	 * @return The closest element.
	 */

	public static int getClosestElementPosition(final DateClassifier target, final List<TodoListAdapterItem> list) {
		// We keep a reference to the target day (in millis).
		final long targetDay = target.getDate().toDateTimeAtStartOfDay().getMillis();

		// We save the last data (compareTo & position).
		long compareToResult = -1;
		int lastPosition = -1;

		// We iterate through the list.
		for(int i = 0; i < list.size(); i++) {
			// We have to check that the element is a Classifier.
			final TodoListAdapterItem element = list.get(i);
			if(!(element instanceof DateClassifier)) {
				continue;
			}

			long result = abs(targetDay - ((DateClassifier)element).getDate().toDateTimeAtStartOfDay().getMillis());

			// If the new compareTo is closer than the previous one, we update the references.
			if(compareToResult < 0 || result < compareToResult) {
				compareToResult = result;
				lastPosition = i;
			}
		}

		// And we return the last position.
		return lastPosition;
	}

	/**
	 * Returns the absolute value of a long a.
	 *
	 * @param a The long.
	 *
	 * @return The absolute value of a.
	 */

	public static long abs(final long a) {
		return a < 0 ? -a : a;
	}

}