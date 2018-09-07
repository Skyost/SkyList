package fr.skyost.skylist.task.adapter;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import fr.skyost.skylist.task.TodoTask;
import fr.skyost.skylist.task.adapter.classifier.Classifier;
import fr.skyost.skylist.util.Function;
import fr.skyost.skylist.util.MultiSortedMap;

/**
 * This task allows to refresh the To-do list adapter.
 */

public class RefreshTodoListAdapterTask extends AsyncTask<Collection<TodoTask>, Void, List<TodoListAdapterItem>> {

	/**
	 * The function that allows to get the Classifier that corresponds to a given task.
	 */

	private static Function<TodoTask, Classifier> classifierFunction;

	/**
	 * The To-do list adapter.
	 */

	private TodoListAdapter adapter;

	/**
	 * Creates a new refresh to-do list adapter task.
	 *
	 * @param adapter The adapter.
	 */

	protected RefreshTodoListAdapterTask(final TodoListAdapter adapter) {
		this.adapter = adapter;
	}

	@SafeVarargs
	@Override
	protected final List<TodoListAdapterItem> doInBackground(final Collection<TodoTask>... collections) {
		// Initializes the classifier function.
		if(classifierFunction == null) {
			classifierFunction = Classifier.getClassifierFunction(adapter.getActivity());
		}

		// We first create a MultiSortedMap, and put tasks like this : key = classifier, values = tasks.
		final MultiSortedMap<Classifier, TodoTask> map = new MultiSortedMap<>();
		final Collection<TodoTask> tasks = collections[0];
		for(final TodoTask task : tasks) {
			map.put(classifierFunction.apply(task), task);
		}

		// Then we add everything in a list : [CLASSIFIER 1, Task 1, ..., Task n, CLASSIFIER 2, Task n + 1, etc...].
		final List<TodoListAdapterItem> result = new ArrayList<>();
		for(final Classifier classifier : map.getAllKeys()) {
			final Set<TodoTask> classifiedTasks = map.get(classifier);
			if(classifiedTasks.isEmpty()) {
				continue;
			}

			result.add(classifier);
			result.addAll(classifiedTasks);
		}

		return result;
	}

	@Override
	protected void onPostExecute(final List<TodoListAdapterItem> result) {
		// We update the items and notify the adapter.
		adapter.setItems(result);
		adapter.notifyDataSetChanged();
	}

	/**
	 * Returns the adapter.
	 *
	 * @return The adapter.
	 */

	public TodoListAdapter getAdapter() {
		return adapter;
	}

	/**
	 * Sets the adapter.
	 *
	 * @param adapter The adapter.
	 */

	public void setAdapter(final TodoListAdapter adapter) {
		this.adapter = adapter;
	}

}