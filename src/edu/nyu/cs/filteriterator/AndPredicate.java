package edu.nyu.cs.filteriterator;

import java.util.ArrayList;
import java.util.List;

/***
 * The logical conjunction of predicates. Accepts an element if and only if
 * the element is accepted by all of the supplied predicates. An
 * AndPredicate can be constructed with two or more arguments, and the returned
 * predicate will be short circuited, i.e. it will return as soon as a
 * predicate returns false.
 * @author William Brantley
 *
 * @param <T> Type accepted by the predicate
 */
public class AndPredicate<T> implements Predicate<T> {
  private final Predicate<T> firstPredicate;
  private final Predicate<T> secondPredicate;
  private final List<Predicate<T>> otherPredicates;
  
  @SafeVarargs
  //rest doesn't depend on subtypes or the fact that it will become
  // Predicate[] at runtime, so this is a safe use of varargs.
  public AndPredicate(Predicate<T> first, Predicate<T> second,
      Predicate<T>... optionalPredicates) {
    if (first == null || second == null) {
      throw new NullPointerException("The first or second predicate is null");
    }
    firstPredicate = first;
    secondPredicate = second;
    otherPredicates = new ArrayList<Predicate<T>>();
    
    for (Predicate<T> p : optionalPredicates) {
      if (p == null) {
        throw new NullPointerException("The third or later predicate is null");
      }
      otherPredicates.add(p);
    }
  }

  @Override
  public boolean accept(T item) {
    boolean result = firstPredicate.accept(item)
        && secondPredicate.accept(item);
    // if the result is false or there are no more predicates, return
    if (!result || otherPredicates.isEmpty()) {
      return result;
    } else {
      for (Predicate<T> p : otherPredicates) {
        if (!p.accept(item)) {
          return false;
        }
      }
      return true;
    }
  }
}