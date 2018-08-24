package fr.skyost.skylist.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * A MultiMap wrapper that is sorted.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */

public class MultiSortedMap<K extends Comparable<K>, V extends Comparable<V>> {

	/**
	 * The map.
	 */

	private final TreeMap<K, Set<V>> map = new TreeMap<>();

	/**
	 * Puts a value in the map.
	 *
	 * @param key The key.
	 * @param value The value.
	 *
	 * @return The previous value.
	 */

	public Set<V> put(final K key, final V value) {
		Set<V> values = map.get(key);
		if(values == null) {
			values = new TreeSet<>();
		}

		values.add(value);
		return map.put(key, values);
	}

	/**
	 * Puts the specified values to the map.
	 *
	 * @param key The key.
	 * @param values The values.
	 *
	 * @return The previous value.
	 */

	public Set<V> putAll(final K key, final Collection<V> values) {
		Set<V> existingValues = map.get(key);
		if(existingValues == null) {
			existingValues = new TreeSet<>();
		}

		existingValues.addAll(values);
		return map.put(key, existingValues);
	}

	/**
	 * Returns the values associated to the specified key.
	 *
	 * @param key The key.
	 *
	 * @return The values.
	 */

	public Set<V> get(final K key) {
		return map.get(key);
	}

	/**
	 * Removes the key.
	 *
	 * @param key The key.
	 *
	 * @return The result of Map.remove(key).
	 */

	public Set<V> remove(final K key) {
		return map.remove(key);
	}

	/**
	 * Removes a value from the map.
	 *
	 * @param key The value's key.
	 * @param value The value.
	 *
	 * @return The result of Collection.remove(value).
	 */

	public boolean remove(final K key, final V value) {
		final Set<V> values = map.get(key);
		if(values == null) {
			return false;
		}

		final boolean result = values.remove(value);
		if(values.isEmpty()) {
			remove(key);
		}

		return result;
	}

	/**
	 * Returns the number of keys.
	 *
	 * @return The number of keys.
	 */

	public int size() {
		return map.size();
	}

	/**
	 * Returns all keys.
	 *
	 * @return All keys.
	 */

	public Set<K> getAllKeys() {
		return map.keySet();
	}

	/**
	 * Returns all values.
	 *
	 * @return All values.
	 */

	public Set<V> getAllValues() {
		final Set<V> values = new TreeSet<>();
		for(final Map.Entry<K, Set<V>> entry : map.entrySet()) {
			values.addAll(entry.getValue());
		}
		return values;
	}

	/**
	 * Clears the map.
	 */

	public void clear() {
		map.clear();
	}

	/*
	// Just an old method that was here when I was overriding this class to provide a model for my To-do tasks.
	protected void changeKey(final V value, final K oldKey, final K newKey) {
		// We get the old list.
		Set<V> values = map.get(oldKey);
		if(values != null) {
			// If possible, we remove the value.
			values.remove(value);
		}

		// We get the new list.
		values = map.get(newKey);
		if(values == null) {
			// If it does not exist, we create it.
			values = new TreeSet<>();
		}

		// We can add it to the new list and put the new list to the map.
		values.add(value);
		map.put(newKey, values);
	}
	*/

}