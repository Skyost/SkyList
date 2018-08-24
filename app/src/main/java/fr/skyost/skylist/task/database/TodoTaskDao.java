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

@Dao
public interface TodoTaskDao {

	@Query("SELECT * FROM " + TodoTask.TABLE_NAME + " ORDER BY description")
	LiveData<List<TodoTask>> getAllTasks();

	@Query("SELECT * FROM " + TodoTask.TABLE_NAME + " WHERE date=:date ORDER BY description")
	LiveData<List<TodoTask>> getTasksForDate(final LocalDate date);

	@Query("SELECT * FROM " + TodoTask.TABLE_NAME + " ORDER BY description")
	List<TodoTask> queryAllTasks();

	@Query("SELECT * FROM " + TodoTask.TABLE_NAME + " WHERE date=:date ORDER BY description")
	List<TodoTask> queryTasksForDate(final LocalDate date);

	@Insert
	long addTask(final TodoTask task);

	@Update
	int updateTask(final TodoTask task);

	@Delete
	void deleteTask(final TodoTask task);

	@Delete
	void deleteTasks(final Collection<TodoTask> tasks);

	@Query("DELETE FROM " + TodoTask.TABLE_NAME)
	void clearTasks();

}