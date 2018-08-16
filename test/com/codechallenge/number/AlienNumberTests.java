package com.codechallenge.number;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
class AlienNumberTests {

  @Test
  @Parameters({})
  public void ShouldCreateAlienNumberWhenValueWithinLimits(int value, String expectedNumeral) {
    AlienNumber number = new AlienNumber(value);
    assertThat(number.toString(),is(expectedNumeral));
  }
  
  @Test
  @Parameters({})
  public void ShouldThrowExceptionWhenValueOutOfLimits(int value) throws Exception {
    assertThrows(RuntimeException.class, () -> {
      new AlienNumber(value);
    });
  }
  
  @Test
  @Parameters({})
  public void ShouldFail_WhenInvalidAlienSymbolIsMapped(
                  String alienSymbol,String romanSymbol) throws RuntimeException{
    assertThrows(IllegalArgumentException.class, () -> {
      AlienNumber.mapToRomanSymbol(alienSymbol, romanSymbol);
    });
  }

  @Test
  @Parameters({})
  public void ShouldFail_WhenInvalidRomanSymbolIsMapped(
                  String alienSymbol,String romanSymbol) throws RuntimeException{
    assertThrows(IllegalArgumentException.class, () -> {
      AlienNumber.mapToRomanSymbol(alienSymbol, romanSymbol);
    });
  }
  
  @Test
  @Parameters({})
  public void ShouldFail_WhenAlienSymbolIsAlreadyMappedToAnotherRomanSymbol(
                  String alienSymbol,String romanSymbol) throws RuntimeException{
    assertThrows(IllegalArgumentException.class, () -> {
      AlienNumber.mapToRomanSymbol(alienSymbol, romanSymbol);
    });
  }
  
  @Test
  @Parameters({})
  public void ShouldFail_WhenRomanSymbolIsAlreadyMappedToAnotherAlienSymbol(
                  String alienSymbol,String romanSymbol) throws RuntimeException{
    assertThrows(IllegalArgumentException.class, () -> {
      AlienNumber.mapToRomanSymbol(alienSymbol, romanSymbol);
    });
  }
  
  @Test
  @Parameters({})
  public void ShouldReturnFalse_WhenMappingAlreadyExists(
                  String alienSymbol,String romanSymbol, boolean expectedResult) {
    assertThat(AlienNumber.mapToRomanSymbol(alienSymbol, romanSymbol), is(expectedResult));
  }
  
  @Test
  @Parameters({})
  public void ShouldReturnTrue_WhenMappingAlreadyExists(
                  String alienSymbol,String romanSymbol, boolean expectedResult) {
    assertThat(AlienNumber.mapToRomanSymbol(alienSymbol, romanSymbol), is(expectedResult));
  }
}
