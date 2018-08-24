package fr.skyost.skylist.util;

/**
 * Represents a very simple function that accepts one argument and that returns one result.
 *
 * @param <T> The argument type.
 * @param <R> The result type.
 */

public interface Function<T, R> {

	/**
	 * Applies the function to the given argument.
	 *
	 * @param arg The argument.
	 *
	 * @return The result.
	 */

	R apply(final T arg);

}