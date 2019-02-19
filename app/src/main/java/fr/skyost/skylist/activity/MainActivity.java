package fr.skyost.skylist.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.kobakei.ratethisapp.RateThisApp;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.skyost.skylist.R;
import fr.skyost.skylist.activity.settings.SettingsActivity;
import fr.skyost.skylist.dialog.EditTaskDialog;
import fr.skyost.skylist.task.TodoTask;
import fr.skyost.skylist.task.TodoTaskModel;
import fr.skyost.skylist.task.adapter.RefreshTodoListAdapterTask;
import fr.skyost.skylist.task.adapter.Selection;
import fr.skyost.skylist.task.adapter.TodoListAdapter;
import fr.skyost.skylist.task.adapter.TodoListAdapterItem;
import fr.skyost.skylist.task.adapter.classifier.date.DateClassifier;
import fr.skyost.skylist.util.Utils;
import fr.skyost.skylist.widget.TriggerWidgetUpdate;

/**
 * This activity represents the launcher activity of the application.
 */

public class MainActivity extends SkyListActivity {

	/**
	 * Selected tasks bundle key.
	 */

	private static final String BUNDLE_SELECTED = "selected";

	/**
	 * LayoutManager state bundle key.
	 */

	private static final String BUNDLE_LAYOUT_MANAGER_STATE = "layoutManagerState";

	/**
	 * RateThisApp bundle key.
	 */

	private static final String BUNDLE_RATETHISAPP = "rateThisApp";

	/**
	 * The settings activity code.
	 */

	private static int ACTIVITY_SETTINGS = 0;

	/**
	 * The dialog that allows to edit tasks.
	 * We have to create it first because it is relatively slow to open.
	 */

	private EditTaskDialog editTaskDialog;

	/**
	 * The model that allows to manipulate the tasks.
	 */

	private TodoTaskModel model;

	/**
	 * The current selection.
	 */

	private Selection selection;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main_layout);

		// Here we get the model and we wait for the tasks.
		final TodoTaskModel model = ViewModelProviders.of(this).get(TodoTaskModel.class);
		model.getTasks().observe(this, new Observer<List<TodoTask>>() {

			@Override
			public void onChanged(@Nullable final List<TodoTask> tasks) {
				// The method must be called once so we have to remove the observer.
				onTodoTasksLoadFinished(model, tasks);
				model.getTasks().removeObserver(this);
			}

		});

		// Let's hide the action bar.
		final ActionBar actionBar = this.getSupportActionBar();
		if(actionBar != null) {
			actionBar.hide();
		}

		// We have to initialize our dialog (see above).
		editTaskDialog = new EditTaskDialog(this);

		if(savedInstanceState == null) {
			return;
		}

		// Let's see if we can restore the selection.
		if(savedInstanceState.containsKey(BUNDLE_SELECTED)) {
			this.getIntent().putExtra(BUNDLE_SELECTED, savedInstanceState.getSerializable(BUNDLE_SELECTED));
		}

		// Here we check whether we can restore the LayoutManager state.
		if(savedInstanceState.containsKey(BUNDLE_LAYOUT_MANAGER_STATE)) {
			final Parcelable parcelable = savedInstanceState.getParcelable(BUNDLE_LAYOUT_MANAGER_STATE);
			this.getIntent().putExtra(BUNDLE_LAYOUT_MANAGER_STATE, parcelable);
		}

		// And here we check if we need to send a request to RateThisApp.
		if(!savedInstanceState.containsKey(BUNDLE_RATETHISAPP)) {
			RateThisApp.init(new RateThisApp.Config(3, 10));
			RateThisApp.onCreate(this);
			RateThisApp.showRateDialogIfNeeded(this);
		}

		// And finally, we clear everything.
		savedInstanceState.clear();
	}

	@Override
	protected void onSaveInstanceState(final Bundle outState) {
		// If it's possible (and needed), we save the selection.
		if(selection != null && selection.size() > 0) {
			outState.putSerializable(BUNDLE_SELECTED, selection.getSelectedPositions());
		}

		// Now we have to save the LayoutManager state.
		final RecyclerView todoRecyclerView = findViewById(R.id.main_todo);
		if(todoRecyclerView != null && todoRecyclerView.getLayoutManager() != null) {
			outState.putParcelable(BUNDLE_LAYOUT_MANAGER_STATE, todoRecyclerView.getLayoutManager().onSaveInstanceState());
		}

		// And we don't have to resend a request to RateThisApp.
		outState.putBoolean(BUNDLE_RATETHISAPP, false);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// We only have one activity, so let's check if this is good.
		if(requestCode != ACTIVITY_SETTINGS) {
			return;
		}

		// If it is needed to restart the activity, then we restart it.
		if(data != null && data.getBooleanExtra(SettingsActivity.BUNDLE_NEED_RESTART, false)) {
			Utils.restart(this);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		getSkyListTheme().apply(this, menu);

		// Those lines of code allow to toggle the items' visibility according to whether there are items in the selection or no.
		final boolean isEmpty = selection == null || selection.size() == 0;
		menu.getItem(0).setVisible(isEmpty);
		menu.getItem(1).setVisible(!isEmpty);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menu_main_add:
			// We open the add task dialog.
			showAddTaskDialog();
			return true;
		case R.id.menu_main_delete:
			// Here we delete the current selection.
			selection.deleteSelection(model);
			invalidateOptionsMenu();
			return true;
		case R.id.menu_main_settings:
			// And here we start the settings activity.
			startActivityForResult(new Intent(this, SettingsActivity.class), ACTIVITY_SETTINGS);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * This method is getting called when all tasks have been loaded.
	 *
	 * @param model The view model.
	 * @param tasks The loaded tasks.
	 */

	private void onTodoTasksLoadFinished(final TodoTaskModel model, final List<TodoTask> tasks) {
		this.model = model;
		this.selection = new Selection();

		// We have to register some observers to update the UI.
		model.getTasks().observe(this, this::refreshTodoListState);
		selection.observe(this, this::refreshSelectionState);

		// Now we can set the action bar visible.
		final ActionBar actionBar = this.getSupportActionBar();
		if(actionBar != null) {
			actionBar.setTitle(R.string.main_title);
			actionBar.show();
		}

		// We set the adapter.
		final TodoListAdapter adapter = new TodoListAdapter(this);
		final RecyclerView todoRecyclerView = findViewById(R.id.main_todo);
		todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		todoRecyclerView.setAdapter(adapter);

		// We restore the changes.
		new RestoreRefreshTodoListAdapterTask(adapter).execute(tasks);
		refreshTodoListState(tasks);

		// If we've started the activity through a shortcut, we open the edit dialog.
		final String action = getIntent().getAction();
		if(action != null && action.equals(Intent.ACTION_INSERT)) {
			showAddTaskDialog();
		}
	}

	/**
	 * Refreshes the To-Do list state (whether it is visible or not).
	 * <br>This method also triggers a widget update.
	 *
	 * @param tasks The tasks list.
	 */

	private void refreshTodoListState(final List<TodoTask> tasks) {
		final boolean isEmpty = tasks.isEmpty();
		findViewById(R.id.main_todo_empty).setVisibility(isEmpty ? View.VISIBLE : View.GONE);
		findViewById(R.id.main_todo).setVisibility(isEmpty ? View.GONE : View.VISIBLE);

		sendBroadcast(new Intent(this, TriggerWidgetUpdate.class));
	}

	/**
	 * Refreshes the selection state (whether menu items are visible or not).
	 *
	 * @param selection The selection.
	 */

	private void refreshSelectionState(final Map<TodoTask, Integer> selection) {
		// If we keep selecting items, we don't have to invalidate the menu.
		if(selection.size() > 1) {
			return;
		}

		invalidateOptionsMenu();
	}

	/**
	 * Shows the add task dialog (which is in fact an edit task dialog).
	 */

	public void showAddTaskDialog() {
		// Here we add a task.
		final TodoTask task = new TodoTask("");
		showEditTaskDialog(task, (dialog, choice) -> model.addTask(task));
	}

	/**
	 * Shows the edit task dialog.
	 *
	 * @param task The task to edit.
	 * @param positiveButtonListener Triggered when the user click on the positive button.
	 */

	public void showEditTaskDialog(final TodoTask task, final DialogInterface.OnClickListener positiveButtonListener) {
		// We set the task to edit.
		editTaskDialog.setTask(task);

		// Then we can change the button listener.
		if(positiveButtonListener != null) {
			editTaskDialog.setOnPositiveButtonClickListener((dialog, choice) -> {
				editTaskDialog.onClick(dialog, choice);
				positiveButtonListener.onClick(dialog, choice);
			});
		}

		// And we show the dialog.
		editTaskDialog.show();
	}

	/**
	 * Returns the To-Do task model.
	 *
	 * @return The To-Do task model.
	 */

	public TodoTaskModel getModel() {
		return model;
	}

	/**
	 * Returns the To-Do task selection.
	 *
	 * @return The To-Do task selection.
	 */

	public Selection getSelection() {
		return selection;
	}

	/**
	 * This task allows to restore some data to the recycler view.
	 */

	private static class RestoreRefreshTodoListAdapterTask extends RefreshTodoListAdapterTask {

		/**
		 * We have to store the calculated position (if we want to use it in onPostExecute).
		 */

		private int scrollToPosition = -1;

		/**
		 * Creates a new task instance.
		 *
		 * @param adapter The To-do list adapter.
		 */

		private RestoreRefreshTodoListAdapterTask(final TodoListAdapter adapter) {
			super(adapter);
		}

		@SafeVarargs
		@Override
		protected final List<TodoListAdapterItem> doInBackground(final Collection<TodoTask>... collections) {
			final List<TodoListAdapterItem> result = super.doInBackground(collections);

			// We get a reference to the required objects.
			final MainActivity activity = getAdapter().getActivity();
			final Intent intent = activity.getIntent();

			// If there is LayoutManager state, we can exit.
			if(intent.hasExtra(BUNDLE_LAYOUT_MANAGER_STATE)) {
				return result;
			}

			// Checks whether we should scroll to today.
			final boolean scrollToToday = activity.getSharedPreferences(SettingsActivity.APP_PREFERENCES, Context.MODE_PRIVATE).getBoolean("list_scrollToToday", true);
			if(!scrollToToday) {
				return result;
			}

			// If everything is ok we get the date classifier.
			final DateClassifier dateClassifier = DateClassifier.getTodayClassifier(activity);
			if(dateClassifier == null) {
				return result;
			}

			// And we calculate the scroll position.
			scrollToPosition = Utils.getClosestElementPosition(dateClassifier, result);
			return result;
		}

		@Override
		protected void onPostExecute(final List<TodoListAdapterItem> result) {
			super.onPostExecute(result);

			// We get a reference to the required objects.
			final MainActivity activity = getAdapter().getActivity();
			final RecyclerView todoRecyclerView = activity.findViewById(R.id.main_todo);
			final TodoListAdapter adapter = (TodoListAdapter)todoRecyclerView.getAdapter();
			final Intent intent = activity.getIntent();

			// And we make the required changes to views visibility.
			activity.findViewById(R.id.main_todo_loading).setVisibility(View.GONE);

			// If there is a selection, we restore it.
			if(intent.hasExtra(BUNDLE_SELECTED)) {
				activity.getSelection().addFromAdapter(adapter, (Integer[])intent.getSerializableExtra(BUNDLE_SELECTED));
				intent.removeExtra(BUNDLE_SELECTED);
			}

			// And if there is LayoutManager state, we also restore it.
			if(intent.hasExtra(BUNDLE_LAYOUT_MANAGER_STATE)) {
				todoRecyclerView.getLayoutManager().onRestoreInstanceState(intent.getParcelableExtra(BUNDLE_LAYOUT_MANAGER_STATE));
				intent.removeExtra(BUNDLE_LAYOUT_MANAGER_STATE);
				return;
			}

			// Otherwise, we scroll to the calculated position.
			if(scrollToPosition != -1) {
				todoRecyclerView.getLayoutManager().scrollToPosition(scrollToPosition);
			}
		}

	}

}