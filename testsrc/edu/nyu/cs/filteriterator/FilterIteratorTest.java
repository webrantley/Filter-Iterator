package edu.nyu.cs.filteriterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import edu.nyu.cs.filteriterator.AndPredicate;
import edu.nyu.cs.filteriterator.FilterIterator;
import edu.nyu.cs.filteriterator.NotPredicate;
import edu.nyu.cs.filteriterator.OrPredicate;
import edu.nyu.cs.filteriterator.Predicate;

public class FilterIteratorTest {
  Predicate<Integer> isEven;
  Predicate<Integer> notZero;
  Predicate<Integer> lessThanOneHundred;
  Predicate<Integer> isNotNull;
  
  @Before
  public void setup() {
    isEven = new Predicate<Integer>() {
      @Override
      public boolean accept(Integer i) {
        return (i % 2) == 0; 
      }
    };
    
    notZero = new Predicate<Integer>() {
      @Override
      public boolean accept(Integer i) {
        return i != 0;
      }
    };
    
    lessThanOneHundred = new Predicate<Integer>() {
      @Override
      public boolean accept(Integer i) {
        return i < 100;
      }
    };
    
    isNotNull = new Predicate<Integer>() {
      @Override
      public boolean accept(Integer i) {
        return i != null;
      }
    };
    
  }
  
  @Test(expected = NullPointerException.class)
  public void testFilter_nullIterator() {
    Iterator<Integer> throwsException =
        new FilterIterator<Integer>(null, isEven);
    throwsException.hasNext();
  }
  
  @Test(expected = NullPointerException.class)
  public void testFilter_nullPredicate() {
    Iterator<Integer> iter = argsIter(0);
    Iterator<Integer> throwsException =
        new FilterIterator<Integer>(iter , null);
    throwsException.hasNext();
  }
  
  @Test(expected = NoSuchElementException.class)
  public void testThrowsException() {
    Iterator<Integer> iter = argsIter();
    FilterIterator<Integer> emptyIter =
        new FilterIterator<Integer>(iter, isEven);
    emptyIter.next();
  }
  
  @Test
  public void testFilter_singlePredicate() {
    Iterator<Integer> iter = argsIter(51, 20, 101, 10, 0);
    Iterator<Integer> filteredIter = new FilterIterator<Integer>(iter, isEven);
    
    assertTrue(filteredIter.hasNext());
    assertEquals(filteredIter.next(), (Integer) 20);
    assertTrue(filteredIter.hasNext());
    assertEquals(filteredIter.next(), (Integer) 10);
    assertTrue(filteredIter.hasNext());
    assertEquals(filteredIter.next(), (Integer) 0);
    assertFalse(filteredIter.hasNext());
  }
  
  @Test
  public void testFilter_acceptsNulls() {
    Iterator<Integer> iter = argsIter(null, 20, null, null, 0, 100);
    // testing for not not null (i.e. is null)
    Iterator<Integer> allNull = 
        new FilterIterator<Integer>(iter, new NotPredicate<Integer>(isNotNull));
    
    assertTrue(allNull.hasNext());
    assertEquals(allNull.next(), null);
    assertTrue(allNull.hasNext());
    assertEquals(allNull.next(), null);
    assertTrue(allNull.hasNext());
    assertEquals(allNull.next(), null);
    assertFalse(allNull.hasNext());

  }
  
  @Test
  public void testComplexPredicate() {
    // Acceptable elements should be non-null, and (odd or less than 100)
    Predicate<Integer> isOdd =
        new NotPredicate<Integer>(isEven);
    
    Predicate<Integer> tempPred =
        new OrPredicate<Integer>(lessThanOneHundred, isOdd);
    
    Predicate<Integer> complexPredicate =
        new AndPredicate<Integer>(isNotNull, tempPred);
    
    Iterator<Integer> testIter = argsIter(null, 50, 101, 1000, null, -3, 5001);
    
    FilterIterator<Integer> filteredIter =
        new FilterIterator<Integer>(testIter, complexPredicate);
    
    assertTrue(filteredIter.hasNext());
    assertEquals(filteredIter.next(), (Integer) 50);
    assertTrue(filteredIter.hasNext());
    assertEquals(filteredIter.next(), (Integer) 101);
    assertTrue(filteredIter.hasNext());
    //For some reason Eclipse didn't like me casting a negative
    assertEquals(filteredIter.next(), new Integer(-3));
    assertTrue(filteredIter.hasNext());
    assertEquals(filteredIter.next(), (Integer) 5001);
    assertFalse(filteredIter.hasNext());
  }
  
  
  private <T> Iterator<T> argsIter(@SuppressWarnings("unchecked") T... args) {
    // The varargs doesn't rely on the fact that an arg could be a subtype or
    // the fact that T... is actually T[] at runtime.
    ArrayList<T> list = new ArrayList<T>();
    for (T arg : args) {
      list.add(arg);
    }
    return list.iterator(); 
  }
}
