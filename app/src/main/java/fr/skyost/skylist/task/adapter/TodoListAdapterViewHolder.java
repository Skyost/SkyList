package fr.skyost.skylist.task.adapter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Represents a To-do list adapter view holder.
 *
 * @param <T> The type of object we can bind to this view holder.
 */

public abstract class TodoListAdapterViewHolder<T extends TodoListAdapterItem> extends RecyclerView.ViewHolder {

	/**
	 * Creates a new To-do list adapter view holder.
	 *
	 * @param itemView The item view.
	 */

	protected TodoListAdapterViewHolder(final View itemView) {
		super(itemView);
	}

	/**
	 * Binds the specified object located at the specified position to this view holder.
	 *
	 * @param arg The object.
	 * @param position The position.
	 */

	public abstract void bind(final T arg, final int position);

}