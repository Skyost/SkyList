package fr.skyost.skylist.dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.joda.time.LocalDate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import fr.skyost.skylist.R;
import fr.skyost.skylist.task.TodoTask;

/**
 * Represents a dialog that allows to edit tasks.
 * As it is slow to open, it is advised to keep a reference in memory.
 */

public class EditTaskDialog extends AlertDialog implements DialogInterface.OnClickListener {

	/**
	 * The layout view.
	 */

	private final View layout;

	/**
	 * The task to edit.
	 */

	private TodoTask task;

	/**
	 * Creates a new edit task dialog instance.
	 *
	 * @param context The Context.
	 */

	public EditTaskDialog(@NonNull final Context context) {
		super(context);

		// Here we inflate the layout and set it as the content view.
		this.layout = View.inflate(context, R.layout.main_dialog_edit, null);
		this.setView(layout);

		// We sets the adapter of the color spinner.
		final Spinner spinner = layout.findViewById(R.id.main_dialog_edit_color);
		spinner.setAdapter(new ColorSpinnerAdapter(context));

		// We set the click listener of the date button.
		final Button date = layout.findViewById(R.id.main_dialog_edit_date);
		date.setOnClickListener(view -> {
			final LocalDate parsed = LocalDate.parse(date.getText().toString());
			new DatePickerDialog(context, (datePicker, year, month, day) -> date.setText(new LocalDate(year, month + 1, day).toString("yyyy-MM-dd")), parsed.getYear(), parsed.getMonthOfYear() - 1, parsed.getDayOfMonth()).show();
		});

		// We set positive and negative button listeners and a dismiss listener that resets the changed listeners.
		setOnPositiveButtonClickListener(this);
		setOnNegativeButtonClickListener(this);
		setOnDismissListener(dialog -> {
			setOnPositiveButtonClickListener(this);
			setOnNegativeButtonClickListener(this);
		});
	}

	@Override
	public void onClick(final DialogInterface dialog, final int choice) {
		// If the user cancelled the dialog, we can exit right away.
		if(dialog != this || choice != BUTTON_POSITIVE) {
			task = null;
			return;
		}

		// If possible, we set the description of our task.
		String description = ((EditText)layout.findViewById(R.id.main_dialog_edit_description)).getText().toString();
		if(description.isEmpty()) {
			description = getContext().getString(R.string.main_todo_task_default_description);
		}
		if(!task.getDescription().equals(description)) {
			task.setDescription(description);
		}

		// Same here for the color.
		final Spinner spinner = layout.findViewById(R.id.main_dialog_edit_color);
		final int selectedColor = ((ColorSpinnerAdapter.Color)spinner.getSelectedItem()).getValue();
		if(task.getColor() != selectedColor) {
			task.setColor(selectedColor);
		}

		// And same here for the date.
		final LocalDate date = LocalDate.parse(((Button)layout.findViewById(R.id.main_dialog_edit_date)).getText().toString());
		if(!task.getDate().equals(date)) {
			task.setDate(date);
		}

		// No need to keep a reference to this task anymore as it useless (a whole new task list will be delivered by the model from the database).
		task = null;
	}

	/**
	 * Returns the task which is going to be edited by the user.
	 *
	 * @return The task which is going to be edited by the user.
	 */

	public TodoTask getTask() {
		return task;
	}

	/**
	 * Sets the task which is going to be edited by the user.
	 *
	 * @param task The task which is going to be edited by the user.
	 */

	public void setTask(final TodoTask task) {
		this.task = task;

		final EditText description = layout.findViewById(R.id.main_dialog_edit_description);
		description.setText(task.getDescription());

		final Spinner spinner = layout.findViewById(R.id.main_dialog_edit_color);
		spinner.setSelection(((ColorSpinnerAdapter)spinner.getAdapter()).getColorValueIndex(task.getColor()));

		final Button date = layout.findViewById(R.id.main_dialog_edit_date);
		date.setText(task.getDate().toString("yyyy-MM-dd"));
	}

	/**
	 * Sets the positive button click listener.
	 *
	 * @param listener The listener.
	 */

	public void setOnPositiveButtonClickListener(final OnClickListener listener) {
		this.setButton(BUTTON_POSITIVE, getContext().getString(android.R.string.ok), listener);
	}

	/**
	 * Sets the negative button click listener.
	 *
	 * @param listener The listener.
	 */

	public void setOnNegativeButtonClickListener(final OnClickListener listener) {
		this.setButton(BUTTON_NEGATIVE, getContext().getString(android.R.string.cancel), listener);
	}

}