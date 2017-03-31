package edu.nyu.cs.filteriterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/***
 * An iterator that filters elements from an iterator that return true
 * to the predicate passed to it. Users can define their own predicates using
 * the Predicate interface, and combine them logically with Ands, Ors, and Nots
 * via the AndPredicate, OrPredicate, and NotPredicate classes found in this
 * package.
 * @author William Brantley
 *
 * @param <T> Type associated with the elements of the passed Iterator
 */

public class FilterIterator<T> implements Iterator<T> {
  private Iterator<T> iterator;
  private Predicate<T> predicate;
  private T nextItem;
  private boolean nextItemFound;
  
  public FilterIterator(Iterator<T> iterator, Predicate<T> predicate) {
    if (iterator == null) {
      throw new NullPointerException("Iterator argument is null");
    } else if (predicate == null) {
      throw new NullPointerException("Predicate argument is null");
    }
    this.iterator = iterator;
    this.predicate = predicate;
    nextItemFound = false;
  }
  
  /***
   * Method that looks ahead to see if there is another element to return. This
   * solves the problem of knowing whether to return hasNext() when even though
   * the iterator has more elements, none of them will pass the predicate. As
   * a side effect, if the next element is found, it stores it in the field
   * nextItem. 
   * @return boolean of whether the iterator has another acceptable element
   */
  private boolean lookAhead() {
    if (nextItemFound) {
      return true;
    }
    while (iterator.hasNext()) {
      T item = iterator.next();
      if(predicate.accept(item)) {
        nextItem = item;
        nextItemFound = true;
        return true;
      }
    }
    return false;
  }
  
  @Override
  public boolean hasNext() {
    return lookAhead();
  }
  
  @Override
  public T next() {
    if (!lookAhead()) {
      throw new NoSuchElementException("No more elements in the iterator");
    }
    nextItemFound = false;
    return nextItem;
  }
}
