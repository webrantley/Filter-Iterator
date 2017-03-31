package edu.nyu.cs.filteriterator;


/***
 * The negation of the the predicate passed. I.e. if the supplied
 * predicate returns true, the NotPredicate of that predicate will
 * return false and vice versa.
 * @author William Brantley
 *
 * @param <T>
 */
public class NotPredicate<T> implements Predicate<T> {
  private final Predicate<T> predicate;
  
  public NotPredicate(Predicate<T> predicate) {
    if (predicate == null) {
      throw new NullPointerException("Predicate passed is null");
    }
    this.predicate = predicate;
  }

  @Override
  public boolean accept(T item) {
    return !predicate.accept(item);
  }
}
