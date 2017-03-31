package edu.nyu.cs.filteriterator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.nyu.cs.filteriterator.AndPredicate;
import edu.nyu.cs.filteriterator.Predicate;

public class AndPredicateTest {
  Predicate<String> longerThanFourChars;
  Predicate<String> hasAdjacentOs;
  Predicate<String> startsWithCapital;
  Predicate<String> throwsException;
  
  @Before
  public void andPredicateTestSetup() {
    longerThanFourChars = new Predicate<String>() {
      @Override
      public boolean accept(String s) {
        return s.length() > 4;
      }
    };
    
    hasAdjacentOs = new Predicate<String>() {
      @Override
      public boolean accept(String s) {
        return s.toLowerCase().contains("oo");
      }
    };
    
    startsWithCapital = new Predicate<String>() {
      @Override
      public boolean accept(String s) {
        return Character.isUpperCase(s.charAt(0));
      }
    };
    
    throwsException = new Predicate<String>() {
      @Override
      public boolean accept(String s) {
        throw new IllegalArgumentException("Automatically thrown exception");
      }
    };
  }
  
  @Test
  public void testShortCircuit_twoPreds() {
    Predicate<String> shouldntThrowException = 
        new AndPredicate<String>(startsWithCapital, throwsException);
    //fails on the first predicate and short circuits
    assertFalse(shouldntThrowException.accept("brooklyn"));
  }
  
  @Test
  public void testShortCircuit_varargsPreds() {
    Predicate<String> shouldntThrowException = 
        new AndPredicate<String>(startsWithCapital, longerThanFourChars,
            hasAdjacentOs, throwsException);
    //fails on the third predicate and short circuits
    assertFalse(shouldntThrowException.accept("Bronx"));
  }

  @Test(expected = NullPointerException.class)
  public void testConstructor_null() {
    AndPredicate<String> throwsException =
        new AndPredicate<String>(longerThanFourChars, null);
    throwsException.accept("Shouldn't reach this point");
  }
  
  @Test(expected = NullPointerException.class)
  public void testConstructor_nullVarargs() {
    AndPredicate<String> throwsException =
        new AndPredicate<String>(longerThanFourChars,
            startsWithCapital, hasAdjacentOs, null);
    throwsException.accept("Shouldn't reach this point");
  }
  
  @Test
  public void testAccept_twoPredicates() {
    Predicate<String> conjunction = 
        new AndPredicate<String>(longerThanFourChars, startsWithCapital);
    
    assertTrue(conjunction.accept("Brooklyn"));
    assertFalse(conjunction.accept("NYC"));
    assertFalse(conjunction.accept("manhattan"));
  }
  
  @Test
  public void testAccept_threePredicates() {
    Predicate<String> conjunction =
        new AndPredicate<String>(longerThanFourChars, hasAdjacentOs,
            startsWithCapital);
    
    assertTrue(conjunction.accept("Brooklyn"));
    assertFalse(conjunction.accept("brooklyn"));
    assertFalse(conjunction.accept("Book"));
    assertFalse(conjunction.accept("Broklyn"));
    assertTrue(conjunction.accept("Moose"));
    assertFalse(conjunction.accept("moose"));
  }
}
