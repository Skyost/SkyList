package fr.skyost.skylist.task.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import fr.skyost.skylist.task.TodoTask;

/**
 * Represents the SkyList database (where tasks are stored).
 */

@Database(entities = {TodoTask.class}, version = 1)
@TypeConverters({LocalDateTypeConverter.class})
public abstract class SkyListDatabase extends RoomDatabase {

	/**
	 * Returns the To-do task DAO (Data Access Object).
	 *
	 * @return The To-do task DAO (Data Access Object).
	 */

	public abstract TodoTaskDao getTodoTaskDao();

}