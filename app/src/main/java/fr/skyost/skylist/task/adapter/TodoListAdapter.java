package fr.skyost.skylist.task.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.skyost.skylist.R;
import fr.skyost.skylist.activity.MainActivity;
import fr.skyost.skylist.task.TodoTask;
import fr.skyost.skylist.task.adapter.holder.ClassifierViewHolder;
import fr.skyost.skylist.task.adapter.holder.TodoTaskViewHolder;

/**
 * A view holder adapter that allows to display To-do tasks and classifiers.
 */

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapterViewHolder> {

	/**
	 * The items to display.
	 */

	private List<TodoListAdapterItem> items = new ArrayList<>();

	/**
	 * The activity instance.
	 */

	private MainActivity activity;

	/**
	 * Creates a new To-do list adapter instance.
	 *
	 * @param activity The activity.
	 */

	public TodoListAdapter(final MainActivity activity) {
		this.activity = activity;

		// We have to observe the task model and the selection to know when to update the adapter.
		activity.getModel().getTasks().observe(activity, tasks -> new RefreshTodoListAdapterTask(this).execute(tasks));
		activity.getSelection().observe(activity, selection -> notifyDataSetChanged());
	}

	@NonNull
	@Override
	public TodoListAdapterViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
		// Thanks to the viewType parameter, we know what kind of object we have to return.
		if(viewType == TodoTask.ADAPTER_ITEM_TYPE) {
			return new TodoTaskViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_todo_task, parent, false), activity);
		}
		return new ClassifierViewHolder((TextView)LayoutInflater.from(parent.getContext()).inflate(R.layout.main_todo_classifier, parent, false), activity);
	}

	@Override
	public void onBindViewHolder(@NonNull final TodoListAdapterViewHolder holder, final int position) {
		holder.bind(items.get(position), position);
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	@Override
	public int getItemViewType(final int position) {
		return items.get(position).getType();
	}

	/**
	 * Returns the item located at the specified position.
	 *
	 * @param position The position.
	 *
	 * @return The item.
	 */

	public TodoListAdapterItem getItemAt(final int position) {
		return items.get(position);
	}

	/**
	 * Returns all items.
	 *
	 * @return All items.
	 */

	public List<TodoListAdapterItem> getItems() {
		return items;
	}

	/**
	 * Sets all items.
	 *
	 * @param items All items.
	 */

	public void setItems(final List<TodoListAdapterItem> items) {
		this.items = items;
	}

	/**
	 * Returns the activity.
	 *
	 * @return The activity.
	 */

	public MainActivity getActivity() {
		return activity;
	}

	/**
	 * Sets the activity.
	 *
	 * @param activity The activity.
	 */

	public void setActivity(final MainActivity activity) {
		this.activity = activity;
	}

}