package ch.unibe.ese.team3.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains some helper methods for handling
 * of Iterables.
 *
 */
public final class ListUtils {
	
	/**
	 * Converts an iterable to a list
	 * @param iterable the iterable to convert
	 * @return a list containing the same elements as the iterable
	 */
	public static <T> List<T> convertToList(Iterable<T> iterable){
		List<T> list = new ArrayList<T>();
		iterable.forEach(item -> list.add(item));
		return list;
	}
	
	/**
	 * Counts the number of elements in an iterable
	 * @param iterable the iterable to count
	 * @return the number of elements in the iterable
	 */
	public static <T> int countIterable(Iterable<T> iterable){
		return convertToList(iterable).size();
	}
}
