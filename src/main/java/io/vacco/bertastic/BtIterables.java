package io.vacco.bertastic;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BtIterables {

  public static <T> List<T> from(Iterable<T> it) {
    var out = new ArrayList<T>();
    it.forEach(out::add);
    return out;
  }

  public static <T> List<T> from(Iterator<T> it) {
    var out = new ArrayList<T>();
    it.forEachRemaining(out::add);
    return out;
  }

  @SuppressWarnings("unchecked")
  public static <T> T[] toArray(Iterable<T> it, Class<T> type) {
    var lst = from(it);
    var out = (T[]) Array.newInstance(type, lst.size());
    return lst.toArray(out);
  }

  @SuppressWarnings("unchecked")
  public static <T> T[] toArray(Iterator<T> it, Class<T> type) {
    var lst = from(it);
    var out = (T[]) Array.newInstance(type, lst.size());
    return lst.toArray(out);
  }

}
