package ch.unibe.ese.team3.util;

import java.util.ArrayList;
import java.util.List;

public final class ListUtils {	
	public static <T> List<T> convertToList(Iterable<T> iterable){
		List<T> list = new ArrayList<T>();
		iterable.forEach(item -> list.add(item));
		return list;
	}
	
	public static <T> int countIterable(Iterable<T> iterable){
		return convertToList(iterable).size();
	}
}
