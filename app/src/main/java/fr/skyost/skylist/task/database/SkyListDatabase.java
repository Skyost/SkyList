package fr.skyost.skylist.task.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import fr.skyost.skylist.task.TodoTask;

@Database(entities = {TodoTask.class}, version = 1)
@TypeConverters({LocalDateTypeConverter.class})
public abstract class SkyListDatabase extends RoomDatabase {

	public abstract TodoTaskDao getTodoTaskDao();

}