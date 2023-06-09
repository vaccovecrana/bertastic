package io.vacco.bertastic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Iterables {

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
  public static <T> T[] toArray(Iterable<T> it) {
    var lst = from(it);
    var out = (T[]) new Object[lst.size()];
    return lst.toArray(out);
  }

  @SuppressWarnings("unchecked")
  public static <T> T[] toArray(Iterator<T> it) {
    var lst = from(it);
    var out = (T[]) new Object[lst.size()];
    return lst.toArray(out);
  }

}
