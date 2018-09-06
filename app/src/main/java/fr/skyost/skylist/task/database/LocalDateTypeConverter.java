package fr.skyost.skylist.task.database;

import android.arch.persistence.room.TypeConverter;

import org.joda.time.LocalDate;

/**
 * Allows to convert a LocalDate to a String (and vice-versa).
 */

public class LocalDateTypeConverter {

	/**
	 * Converts the specified String to a LocalDate.
	 *
	 * @param value The String.
	 *
	 * @return The LocalDate.
	 */

	@TypeConverter
	public static LocalDate toDate(final String value) {
		return LocalDate.parse(value);
	}

	/**
	 * Converts the specified LocalDate to a String.
	 *
	 * @param value The LocalDate.
	 *
	 * @return The String.
	 */

	@TypeConverter
	public static String toString(final LocalDate value) {
		return value.toString("yyyy-MM-dd");
	}

}