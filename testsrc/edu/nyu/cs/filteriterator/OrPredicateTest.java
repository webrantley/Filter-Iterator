package edu.nyu.cs.filteriterator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.nyu.cs.filteriterator.OrPredicate;
import edu.nyu.cs.filteriterator.Predicate;

public class OrPredicateTest {
  Predicate<String> longerThanFourChars;
  Predicate<String> hasAdjacentOs;
  Predicate<String> startsWithCapital;
  Predicate<String> throwsException;

  
  @Before
  public void orPredicateTestSetup() {
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
        new OrPredicate<String>(startsWithCapital, throwsException);
    //passes on the first predicate and short circuits
    assertTrue(shouldntThrowException.accept("Brooklyn"));
  }
  
  @Test
  public void testShortCircuit_varargsPreds() {
    Predicate<String> shouldntThrowException = 
        new OrPredicate<String>(startsWithCapital, longerThanFourChars,
            hasAdjacentOs, throwsException);
    //passes on the third predicate and short circuits
    assertTrue(shouldntThrowException.accept("book"));
  }
  
  @Test(expected = NullPointerException.class)
  public void testConstructor_null() {
    OrPredicate<String> throwsException =
        new OrPredicate<String>(longerThanFourChars, null);
    throwsException.accept("Shouldn't reach this point");
  }
  @Test(expected = NullPointerException.class)
  public void testConstructor_nullVarargs() {
    Predicate<String> throwsException = 
        new OrPredicate<String>(longerThanFourChars, hasAdjacentOs,
            startsWithCapital, null);
    throwsException.accept("Shouldn't reach this point");
  }
  
  @Test
  public void testAccept_twoPreds() {
    Predicate<String> disjunction = 
        new OrPredicate<String>(longerThanFourChars, startsWithCapital);
    
    assertTrue(disjunction.accept("A"));
    assertTrue(disjunction.accept("bronx"));
    assertTrue(disjunction.accept("Brooklyn"));
    assertFalse(disjunction.accept("a"));
  }
  
  @Test
  public void testAccept_threePreds() {
    Predicate<String> disjunction = 
        new OrPredicate<String>(longerThanFourChars, startsWithCapital,
            hasAdjacentOs);
    
    assertTrue(disjunction.accept("Brooklyn"));
    assertTrue(disjunction.accept("Bronx"));
    assertTrue(disjunction.accept("brooklyn"));
    assertTrue(disjunction.accept("Boo"));
    assertTrue(disjunction.accept("manhattan"));
    assertTrue(disjunction.accept("NYC"));
    assertTrue(disjunction.accept("oo"));
    assertFalse(disjunction.accept("nyc"));
  }
}