package edu.nyu.cs.filteriterator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.nyu.cs.filteriterator.NotPredicate;
import edu.nyu.cs.filteriterator.Predicate;

public class NotPredicateTest {  
//  private <T> Iterator<T> argsIter(T... args) {
//    ArrayList<T> list = new ArrayList<T>();
//    for (T arg : args) {
//      list.add(arg);
//    }
//    return list.iterator(); 
//  }
  
  @Test(expected = NullPointerException.class)
  public void testNotPredicateConstructor_null() {
    NotPredicate<String> throwsException = new NotPredicate<String>(null);
    throwsException.accept("Shouldn't reach this point");
  }
  
  @Test
  public void testNotPredicate_String() {
    Predicate<String> isLongerThanFive = new Predicate<String>() {
      public boolean accept(String s) {
        return s.length() > 5;
      }
    };
    
    Predicate<String> notLongerThanFive =
        new NotPredicate<String>(isLongerThanFive);
    
    assertTrue(notLongerThanFive.accept("NYU"));
    assertFalse(notLongerThanFive.accept("Brooklyn"));
    assertTrue(notLongerThanFive.accept(""));
  }
  
  @Test
  public void testNotPredicate_Integer() {
    Predicate<Integer> isPositive = new Predicate<Integer>() {
      public boolean accept(Integer i) {
        return i > 0;
      }
    };
    Predicate<Integer> isNotPositive =
        new NotPredicate<Integer>(isPositive);
    
    assertTrue(isNotPositive.accept(0));
    assertFalse(isNotPositive.accept(100));
    assertTrue(isNotPositive.accept(-1));
  }
}
