package com.codechallenge.number;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class RomanNumberTests {
  
  @Test
  @Parameters(
      {"1,I","2,II","3,III","4,IV", "5,V","6,VI","7,VII","8,VIII","9,IX", "10,X",
       "20,XX", "30,XXX", "40,XL", "50,L","60,LX", "70,LXX", "80,LXXX", "90,XC",
       "100,C", "200,CC", "300,CCC","400,CD", "500,D", "600,DC", "700,DCC", "800,DCCC", "900,CM",
       "1000,M", "2000,MM", "3000,MMM", "2018,MMXVIII","3999,MMMCMXCIX", "79,LXXIX"})
  public void ShouldCreateRomanNumbersWhenValueWithinLimits(int integerValue, String expectedNumeral) {
    RomanNumber romanNumber = new RomanNumber(integerValue);
    assertThat(romanNumber.toString(), is(expectedNumeral));
  }
  
  @Test
  @Parameters({"0","4000","-200","5000"})
  public void ShouldThrowExceptionWhenValueOutsideLimits(int integerValue) throws Exception {
    assertThrows(RuntimeException.class,  () -> { 
      new RomanNumber(integerValue);
    });
  }
  
  @Test
  @Parameters(
      {"1,I","2,II","3,III","4,IV", "5,V","6,VI","7,VII","8,VIII","9,IX", "10,X",
        "20,XX", "30,XXX", "40,XL", "50,L","60,LX", "70,LXX", "80,LXXX", "90,XC",
        "100,C", "200,CC", "300,CCC","400,CD", "500,D", "600,DC", "700,DCC", "800,DCCC", "900,CM",
        "1000,M", "2000,MM", "3000,MMM", "2018,MMXVIII","3999,MMMCMXCIX", "79,LXXIX"})
  public void ShouldCreateSuccessfullyWhenValidRomanNumeral(int value, String numeral) {
    RomanNumber romanNumber = new RomanNumber(numeral);
    assertThat(romanNumber.value(), is(value));
  }
  
  @Test
  @Parameters({"IIII","IVIV","IVVX"  })
  public void ShouldThrowExceptionWhenIllegalRomanNumeral(String numeral) throws Exception{
    assertThrows(RuntimeException.class,  () -> {
      new RomanNumber(numeral);
    });      
  }
}
