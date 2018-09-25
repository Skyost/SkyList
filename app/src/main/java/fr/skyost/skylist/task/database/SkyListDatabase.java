package fr.skyost.skylist.task.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
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