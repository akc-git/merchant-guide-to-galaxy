package com.codechallenge.number;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class AlienNumberTests {

  @BeforeAll
  static void mapAlienSymbols() {
    AlienNumber.mapToRomanSymbol("glob",   "I");
    AlienNumber.mapToRomanSymbol("prok",   "V");
    AlienNumber.mapToRomanSymbol("pish",   "X");
    AlienNumber.mapToRomanSymbol("tegj",   "L");
    AlienNumber.mapToRomanSymbol("yibai",  "C");
    AlienNumber.mapToRomanSymbol("wubai",  "D");
    //  AlienNumber.mapToRomanSymbol("yiqian", "M");    
  }

  @ParameterizedTest
  @CsvSource(
      {"12, pish glob glob",
        "599, wubai pish yibai glob pish"})
  public void ShouldCreateAlienNumberWhenValueWithinLimits(int value, String expectedNumeral) {
    AlienNumber number = new AlienNumber(value);
    assertThat(number.toString(),is(expectedNumeral));
  }

  //@Test
  //@Disabled
  //public void ShouldThrowExceptionWhenValueOutOfLimits(int value) throws Exception {
  //  assertThrows(RuntimeException.class, () -> {
  //    new AlienNumber(value);
  //  });
  //}
  //

  @ParameterizedTest
  @CsvSource(
      {"invalid symbol,M"})
  public void ShouldFail_WhenInvalidAlienSymbolIsMapped(
      String alienSymbol,String romanSymbol) throws RuntimeException{
    assertThrows(IllegalArgumentException.class, () -> {
      AlienNumber.mapToRomanSymbol(alienSymbol, romanSymbol);
    });
  }

  @ParameterizedTest
  @CsvSource(
      {"glob, M"})
  public void ShouldFail_WhenInvalidRomanSymbolIsMapped(
                  String alienSymbol,String romanSymbol) throws RuntimeException{
    assertThrows(IllegalArgumentException.class, () -> {
      AlienNumber.mapToRomanSymbol(alienSymbol, romanSymbol);
    });
  }

  @ParameterizedTest
  @CsvSource(
      {"glob, N"})
  public void ShouldFail_WhenAlienSymbolIsAlreadyMappedToAnotherRomanSymbol(
      String alienSymbol,String romanSymbol) throws RuntimeException{
    assertThrows(IllegalArgumentException.class, () -> {
      AlienNumber.mapToRomanSymbol(alienSymbol, romanSymbol);
    });
  }

  @ParameterizedTest
  @CsvSource(
      {"somesymbol,I"})
  public void ShouldFail_WhenRomanSymbolIsAlreadyMappedToAnotherAlienSymbol(
      String alienSymbol,String romanSymbol) throws RuntimeException{
    assertThrows(IllegalArgumentException.class, () -> {
      AlienNumber.mapToRomanSymbol(alienSymbol, romanSymbol);
    });
  }

  @ParameterizedTest
  @CsvSource(
      {"yiqian, M"})
  public void ShouldReturnTrue_WhenMappedCorrectly(String alienSymbol,String romanSymbol) {
    assertTrue(AlienNumber.mapToRomanSymbol(alienSymbol, romanSymbol));
  } 

  @ParameterizedTest
  @CsvSource(
      {"yiqian, M"})
  public void ShouldReturnFalse_WhenMappingAlreadyExists(
      String alienSymbol,String romanSymbol){
    assertFalse(AlienNumber.mapToRomanSymbol(alienSymbol, romanSymbol));
  } 

}
