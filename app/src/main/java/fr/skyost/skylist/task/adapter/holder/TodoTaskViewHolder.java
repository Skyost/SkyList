package fr.skyost.skylist.task.adapter.holder;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import fr.skyost.skylist.R;
import fr.skyost.skylist.activity.MainActivity;
import fr.skyost.skylist.application.SkyListApplication;
import fr.skyost.skylist.application.theme.SkyListTheme;
import fr.skyost.skylist.task.TodoTask;
import fr.skyost.skylist.task.adapter.TodoListAdapterViewHolder;
import fr.skyost.skylist.view.ColorCircleView;

/**
 * A view holder that holds To-do tasks.
 */

public class TodoTaskViewHolder extends TodoListAdapterViewHolder<TodoTask> {

	/**
	 * The main activity instance.
	 */

	private MainActivity activity;

	/**
	 * The application theme.
	 */

	private final SkyListTheme theme;

	/**
	 * The color circle view.
	 */

	private ColorCircleView colorCircle;

	/**
	 * The description TextView.
	 */

	private TextView description;

	/**
	 * The date TextView.
	 */

	private TextView date;

	/**
	 * Creates a new task view holder.
	 *
	 * @param itemView The item view.
	 * @param activity The activity instance.
	 */

	public TodoTaskViewHolder(final View itemView, final MainActivity activity) {
		super(itemView);

		this.activity = activity;
		this.theme = ((SkyListApplication)activity.getApplication()).getSkyListTheme();

		// We have to apply the theme on this view holder.
		colorCircle = itemView.findViewById(R.id.main_todo_colorcircle);
		description = itemView.findViewById(R.id.main_todo_description);
		date = itemView.findViewById(R.id.main_todo_date);

		description.setTextColor(ContextCompat.getColor(activity, theme.getTodoTaskDescriptionColor()));
		date.setTextColor(ContextCompat.getColor(activity, theme.getTodoTaskDateColor()));
	}

	@Override
	public void bind(final TodoTask task, final int position) {
		// Here we bind task with its position on the current view holder.
		colorCircle.setColor(task.getColor());
		description.setText(task.getDescription());
		date.setText(SimpleDateFormat.getDateInstance().format(task.getDate().toDate()));
		itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), activity.getSelection().isInSelection(task) ? theme.getTodoTaskSelectedBackgroundColor() : theme.getPrimaryColor()));

		// And we set the click listeners.
		itemView.setOnClickListener(view -> {
			if(activity.getSelection().size() > 0) {
				activity.getSelection().toggleSelection(task, position);
				return;
			}
			activity.showEditTaskDialog(task, (dialog, choice) -> activity.getModel().updateTask(task));
		});
		itemView.setOnLongClickListener(view -> {
			activity.getSelection().toggleSelection(task, position);
			return true;
		});
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

	/**
	 * Returns the color circle view.
	 *
	 * @return The color circle view.
	 */

	public ColorCircleView getColorCircle() {
		return colorCircle;
	}

	/**
	 * Sets the color circle view.
	 *
	 * @param colorCircle The color circle view.
	 */

	public void setColorCircle(final ColorCircleView colorCircle) {
		this.colorCircle = colorCircle;
	}

	/**
	 * Returns the description TextView.
	 *
	 * @return The description TextView.
	 */

	public TextView getDescription() {
		return description;
	}

	/**
	 * Sets the description TextView.
	 *
	 * @param description The description TextView.
	 */

	public void setDescription(final TextView description) {
		this.description = description;
	}

	/**
	 * Returns the date TextView.
	 *
	 * @return The date TextView.
	 */

	public TextView getDate() {
		return date;
	}

	/**
	 * Sets the date TextView.
	 *
	 * @param date The date TextView
	 */

	public void setDate(final TextView date) {
		this.date = date;
	}

}
