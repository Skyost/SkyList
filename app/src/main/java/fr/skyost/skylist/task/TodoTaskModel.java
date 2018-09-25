package fr.skyost.skylist.task;

import android.app.Application;
import android.os.AsyncTask;

import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import fr.skyost.skylist.application.SkyListApplication;
import fr.skyost.skylist.task.database.TodoTaskDao;

/**
 * The view model that allows to perform some manipulations on the database.
 */

public class TodoTaskModel extends AndroidViewModel {

	/**
	 * The tasks LiveData.
	 */

	private final LiveData<List<TodoTask>> tasks;

	/**
	 * The DAO.
	 */

	private final TodoTaskDao table;

	/**
	 * Creates a new model instance.
	 *
	 * @param application The application (must be a SkyListApplication).
	 */

	public TodoTaskModel(@NonNull final Application application) {
		super(application);

		table = getApplication().getDatabase().getTodoTaskDao();
		tasks = table.getAllTasks();
	}

	/**
	 * Returns the tasks LiveData.
	 *
	 * @return The tasks LiveData.
	 */

	public LiveData<List<TodoTask>> getTasks() {
		return tasks;
	}

	/**
	 * Adds a task.
	 *
	 * @param task The task.
	 */

	public void addTask(final TodoTask task) {
		AsyncTask.execute(() -> task.setId(table.addTask(task)));
	}

	/**
	 * Deletes a task.
	 *
	 * @param task The task.
	 */

	public void deleteTask(final TodoTask task) {
		AsyncTask.execute(() -> table.deleteTask(task));
	}

	/**
	 * Deletes all tasks from the specified Collection.
	 *
	 * @param tasks The Collection.
	 */

	public void deleteTasks(final Collection<TodoTask> tasks) {
		AsyncTask.execute(() -> table.deleteTasks(tasks));
	}

	/**
	 * Updates a task.
	 *
	 * @param task The task.
	 */

	public void updateTask(final TodoTask task) {
		AsyncTask.execute(() -> table.updateTask(task));
	}

	@Override
	@NonNull
	public SkyListApplication getApplication() {
		return super.getApplication();
	}

}