package edu.nyu.cs.filteriterator;
/***
 * Predicate interface. Implementing this should provide an accept method
 * that returns true if the item passed to it would is true if passed to the
 * predicate.
 * @author Schidlowsky
 *
 * @param <T> Type of element passed to the Predicate
 */
public interface Predicate<T> {
  /***
   * Returns true if the item meets the conditions of the predicate. 
   * @param item Item to be passed to the predicate
   * @return boolean of whether the item passes the predicate
   */
  boolean accept(T item);
}