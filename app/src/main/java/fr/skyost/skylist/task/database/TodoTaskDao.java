package fr.skyost.skylist.task.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.joda.time.LocalDate;

import java.util.Collection;
import java.util.List;

import fr.skyost.skylist.task.TodoTask;

/**
 * The To-do task Data Access Object.
 */

@Dao
public interface TodoTaskDao {

	/**
	 * Returns all tasks as a LiveData (ordered by their description).
	 *
	 * @return All tasks as a LiveData (ordered by their description).
	 */

	@Query("SELECT * FROM " + TodoTask.TABLE_NAME + " ORDER BY description")
	LiveData<List<TodoTask>> getAllTasks();

	/**
	 * Returns all tasks as a LiveData of the specified date.
	 *
	 * @param date The date.
	 *
	 * @return All tasks as a LiveData of the specified date.
	 */

	@Query("SELECT * FROM " + TodoTask.TABLE_NAME + " WHERE date=:date ORDER BY description")
	LiveData<List<TodoTask>> getTasksForDate(final LocalDate date);

	/**
	 * Returns all tasks (ordered by their description).
	 *
	 * @return All tasks (ordered by their description).
	 */

	@Query("SELECT * FROM " + TodoTask.TABLE_NAME + " ORDER BY description")
	List<TodoTask> queryAllTasks();

	/**
	 * Returns all tasks of the specified date.
	 *
	 * @param date The date.
	 *
	 * @return All tasks of the specified date.
	 */

	@Query("SELECT * FROM " + TodoTask.TABLE_NAME + " WHERE date=:date ORDER BY description")
	List<TodoTask> queryTasksForDate(final LocalDate date);

	/**
	 * Adds a task to the table.
	 *
	 * @param task The task to add.
	 *
	 * @return The task's ID.
	 */

	@Insert
	long addTask(final TodoTask task);

	/**
	 * Saves the specified task to the database.
	 *
	 * @param task The task to update.
	 */

	@Update
	void updateTask(final TodoTask task);

	/**
	 * Deletes a single task.
	 *
	 * @param task The task to delete.
	 */

	@Delete
	void deleteTask(final TodoTask task);

	/**
	 * Deletes the specified tasks.
	 *
	 * @param tasks The tasks to delete.
	 */

	@Delete
	void deleteTasks(final Collection<TodoTask> tasks);

	/**
	 * Clears all tasks from the table.
	 */

	@Query("DELETE FROM " + TodoTask.TABLE_NAME)
	void clearTasks();

}