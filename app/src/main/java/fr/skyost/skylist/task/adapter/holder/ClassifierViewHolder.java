package fr.skyost.skylist.task.adapter.holder;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import fr.skyost.skylist.activity.MainActivity;
import fr.skyost.skylist.task.adapter.TodoListAdapterViewHolder;
import fr.skyost.skylist.task.adapter.classifier.Classifier;

/**
 * A view holder that holds classifiers name.
 */

public class ClassifierViewHolder extends TodoListAdapterViewHolder<Classifier> {

	/**
	 * The main activity instance.
	 */

	private MainActivity activity;

	/**
	 * The text view.
	 */

	private TextView itemView;

	/**
	 * Creates a new classifier view holder instance.
	 *
	 * @param itemView The text view.
	 * @param activity The main activity instance.
	 */

	public ClassifierViewHolder(final TextView itemView, final MainActivity activity) {
		super(itemView);

		this.activity = activity;
		this.itemView = itemView;

		// We have to apply the theme on this view holder.
		itemView.setTextColor(ContextCompat.getColor(activity, activity.getSkyListTheme().getClassifierColor()));
	}

	@Override
	public void bind(final Classifier classifier, final int position) {
		itemView.setText(classifier.getDisplayString(activity.getResources()));
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
	 * Returns the text view.
	 *
	 * @return The text view.
	 */

	public TextView getItemView() {
		return itemView;
	}

	/**
	 * Sets the text view.
	 *
	 * @param itemView The text view.
	 */

	public void setItemView(final TextView itemView) {
		this.itemView = itemView;
	}

}
