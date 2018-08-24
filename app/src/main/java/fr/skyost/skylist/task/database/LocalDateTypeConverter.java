package fr.skyost.skylist.task.database;

import android.arch.persistence.room.TypeConverter;

import org.joda.time.LocalDate;

public class LocalDateTypeConverter {

	@TypeConverter
	public static LocalDate toDate(final String value) {
		return LocalDate.parse(value);
	}

	@TypeConverter
	public static String toString(final LocalDate value) {
		return value.toString("yyyy-MM-dd");
	}

}