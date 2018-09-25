package fr.skyost.skylist.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import fr.skyost.skylist.R;
import fr.skyost.skylist.view.ColorCircleView;

/**
 * The color spinner adapter, that allows to display colors with their name.
 */

public class ColorSpinnerAdapter extends ArrayAdapter<ColorSpinnerAdapter.Color> {

	/**
	 * The color list.
	 */

	private final List<Color> colors = new ArrayList<>();

	/**
	 * Creates a new color spinner adapter instance.
	 *
	 * @param context The Context.
	 */

	ColorSpinnerAdapter(@NonNull final Context context) {
		super(context, R.layout.main_dialog_edit_color_item);

		// We get the colors name and value and we put them in a list.
		final String[] names = context.getResources().getStringArray(R.array.main_classifier_colors);
		final int[] values = context.getResources().getIntArray(R.array.todo_task_available_colors);
		for(int i = 0; i < values.length; i++) {
			colors.add(new Color(names[i], values[i]));
		}
	}

	@NonNull
	@Override
	public View getView(final int position, final View convertView, @NonNull final ViewGroup parent) {
		// If we're not in the dropdown, we have to adapt a little bit.
		final RelativeLayout layout = (RelativeLayout)getDropDownView(position, convertView, parent);
		layout.setPadding(0, 0, 0, 0);
		layout.setGravity(Gravity.CENTER);
		return layout;
	}

	@Override
	public View getDropDownView(final int position, View convertView, @NonNull final ViewGroup parent) {
		// If we do not have inflated a view previously, we create it and put it as a tag; otherwise we only have to get it back from the convert view tag.
		final ViewHolder viewHolder;
		if(convertView == null) {
			final LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
			convertView = inflater.inflate(R.layout.main_dialog_edit_color_item, parent, false);
			viewHolder = new ViewHolder(convertView.findViewById(R.id.main_dialog_edit_color_circlecolor), convertView.findViewById(R.id.main_dialog_edit_color_name));
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		// And we update our view according to the current color data.
		final Color color = colors.get(position);
		viewHolder.getColorCircle().setColor(color.getValue());
		viewHolder.getName().setText(color.getName());
		return convertView;
	}

	@Override
	public Color getItem(final int index) {
		return colors.get(index);
	}

	@Override
	public int getCount() {
		return colors.size();
	}

	/**
	 * Returns the index of the specified color value.
	 *
	 * @param colorValue The color value.
	 *
	 * @return The index.
	 */

	public int getColorValueIndex(final int colorValue) {
		final int n = colors.size();
		for(int i = 0; i < n; i++) {
			if(colorValue == colors.get(i).getValue()) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Represents an immutable color with a name and a value.
	 */

	class Color {

		/**
		 * The name.
		 */

		private final String name;

		/**
		 * The value.
		 */

		private final int value;

		/**
		 * Creates a new color instance.
		 *
		 * @param name The name.
		 * @param value The value.
		 */

		private Color(final String name, final int value) {
			this.name = name;
			this.value = value;
		}

		/**
		 * Returns the color's name.
		 *
		 * @return The color's name.
		 */

		public String getName() {
			return name;
		}

		/**
		 * Returns the color's value.
		 *
		 * @return The color's value.
		 */

		public int getValue() {
			return value;
		}

	}

	/**
	 * Represents a view holder.
	 */

	private class ViewHolder {

		/**
		 * The held color circle.
		 */

		private ColorCircleView colorCircle;

		/**
		 * The held name text view.
		 */

		private TextView name;

		/**
		 * Creates a new view holder instance.
		 *
		 * @param colorCircle The color circle to hold.
		 * @param name The text view to hold.
		 */

		ViewHolder(final ColorCircleView colorCircle, final TextView name) {
			this.colorCircle = colorCircle;
			this.name = name;
		}

		/**
		 * Returns the held color circle.
		 *
		 * @return The held color circle.
		 */

		private ColorCircleView getColorCircle() {
			return colorCircle;
		}

		/**
		 * Sets the color circle to hold.
		 *
		 * @param colorCircle The color circle to hold.
		 */

		public void setColorCircle(final ColorCircleView colorCircle) {
			this.colorCircle = colorCircle;
		}

		/**
		 * Returns the name text view.
		 *
		 * @return The name text view.
		 */

		public TextView getName() {
			return name;
		}

		/**
		 * Sets the name text view to hold.
		 *
		 * @param name The name text view to hold.
		 */

		public void setName(final TextView name) {
			this.name = name;
		}

	}

}