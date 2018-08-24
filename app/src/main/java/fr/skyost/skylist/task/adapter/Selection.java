package fr.skyost.skylist.task.adapter;

import android.arch.lifecycle.LiveData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import fr.skyost.skylist.task.TodoTask;
import fr.skyost.skylist.task.TodoTaskModel;

/**
 * Represents a selection of To-do task.
 */

public class Selection extends LiveData<Map<TodoTask, Integer>> {

	/**
	 * The selection (key : the task, value : the position in the adapter).
	 */

	private final HashMap<TodoTask, Integer> selection = new HashMap<>();

	/**
	 * Toggles the selection of a specified task.
	 *
	 * @param task The task.
	 * @param positionInAdapter The position in adapter.
	 */

	public void toggleSelection(final TodoTask task, final int positionInAdapter) {
		// We check if the task is the selection : if yes we remove it, else we add it.
		if(isInSelection(task)) {
			removeFromSelection(task);
			return;
		}

		addToSelection(task, positionInAdapter);
	}

	/**
	 * Adds a task to the selection.
	 *
	 * @param task The task.
	 * @param positionInAdapter Its position in the adapter.
	 */

	public void addToSelection(final TodoTask task, final int positionInAdapter) {
		selection.put(task, positionInAdapter);
		setValue(selection);
	}

	/**
	 * Returns whether the specified task is in the selection.
	 *
	 * @param task The task.
	 *
	 * @return Whether the specified task is in the selection.
	 */

	public boolean isInSelection(final TodoTask task) {
		return selection.containsKey(task);
	}

	/**
	 * Removes a task from the selection.
	 *
	 * @param task The task.
	 */

	public void removeFromSelection(final TodoTask task) {
		final Integer positionInRecyclerView = selection.remove(task);
		if(positionInRecyclerView == null) {
			return;
		}

		setValue(selection);
	}

	/**
	 * Returns the selection size.
	 *
	 * @return The selection size.
	 */

	public int size() {
		return selection.size();
	}

	/**
	 * Clears the selection.
	 */

	public void clearSelection() {
		selection.clear();
		setValue(selection);
	}

	/**
	 * Deletes all tasks from the selection (and triggers a deletion from the specified model).
	 *
	 * @param model The model.
	 */

	public void deleteSelection(final TodoTaskModel model) {
		model.deleteTasks(new HashSet<>(this.selection.keySet()));
		clearSelection();
	}

	/**
	 * Returns the positions of selected tasks.
	 *
	 * @return The positions of selected tasks.
	 */

	public Integer[] getSelectedPositions() {
		return selection.values().toArray(new Integer[0]);
	}

	/**
	 * Restores this selection from the specified adapter and positions.
	 *
	 * @param adapter The adapter.
	 * @param selectedPositions The selected positions.
	 */

	public void addFromAdapter(final TodoListAdapter adapter, final Integer... selectedPositions) {
		for(final int selectedPosition : selectedPositions) {
			selection.put((TodoTask)adapter.getItemAt(selectedPosition), selectedPosition);
		}
	}

}