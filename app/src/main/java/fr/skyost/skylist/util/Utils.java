package fr.skyost.skylist.util;

import java.util.List;

import fr.skyost.skylist.task.adapter.TodoListAdapterItem;
import fr.skyost.skylist.task.adapter.classifier.Classifier;

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

	public static int getClosestElementPosition(final Classifier target, final List<TodoListAdapterItem> list) {
		// We save the last data (compareTo & position).
		int compareToResult = 10;
		int lastPosition = -1;

		// We iterate through the list.
		for(int i = 0; i < list.size(); i++) {
			// We have to check that the element is a Classifier.
			final TodoListAdapterItem element = list.get(i);
			if(!(element instanceof Classifier)) {
				continue;
			}

			int result = Math.abs(target.compareTo((Classifier)element));

			// If the new compareTo is closer than the previous one, we update the references.
			if(result < compareToResult) {
				compareToResult = result;
				lastPosition = i;
			}
		}

		// And we return the last position.
		return lastPosition;
	}

}