package com.codechallenge.number;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RomanNumber {

  public enum Digit {

    I(1), V(5), X(10), L(50), C(100), D(500), M(1000);

    private int value;

    Digit(int value) {
      this.value = value;
    }

    public int value() {
      return value;
    }
  }

  private int value;
  private String numeral;

  private static final String REPEATABLE_DIGITS = "IXCM";
  private static final String NON_REPEATABLE_DIGITS = "DLV";
  private static final String NON_SUBTRACTED_DIGITS = "DLV";
  private static final String VALID_SUBTRACTIVE_PAIRS = "(?:I(?:V|X))|(?:X(?:L|C))|(?:C(?:D|M))";
  private static final String[][] pattern = { { "", "\\l", "\\l\\l", "\\l\\l\\l", "\\l\\h", "" },
                                              { "\\l", "\\l\\p", "\\l\\p\\p", "\\l\\p\\p\\p", "\\p\\h" } };

  public static final int MIN_VALUE = 1;
  public static final int MAX_VALUE = 3999;

  RomanNumber(String numeral) {
    this.value = parseRomanNumeral(numeral);
    this.numeral = numeral.toUpperCase();
  }

  public RomanNumber(int value) {
    this.numeral = convertToRomanNumeral(value);
    this.value = value;
  }

  public int value() {
    return this.value;
  }

  @Override
  public String toString() {
    return numeral;
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof RomanNumber) {
      return value == ((RomanNumber) object).value();
    }
    return false;
  }

  private static String convertToRomanNumeral(int value) {

    if (value < MIN_VALUE || value > MAX_VALUE)
      throw new IllegalArgumentException();

    List<Integer> additives = breakIntoAdditives(value);

    return additives.stream()
        .map(Integer::intValue)
        .map(RomanNumber::_convertToRomanNumeral)
        .collect(Collectors.joining());
  }

  private static String _convertToRomanNumeral(int value) {

    int n = Digit.values().length;
    double e;
    int stepSize;
    int repeats;
    Digit low = Digit.values()[0];
    Digit prev = Digit.values()[0];
    Digit high;

    for (int i = 1; i < n; i++) {
      high = Digit.values()[i];
      if (value >= low.value() && value < high.value()) {
        e = Math.log10(low.value());

        if (Math.floor(e) == e) {
          repeats = value / low.value();
          return pattern[0][repeats]
              .replaceAll("\\\\l", low.name())
              .replaceAll("\\\\h", high.name());
        } else {
          stepSize = (high.value() - low.value()) / 5;
          repeats = (value - low.value()) / stepSize;
          return pattern[1][repeats]
              .replaceAll("\\\\l", low.name())
              .replaceAll("\\\\p", prev.name())
              .replaceAll("\\\\h", high.name());
        }
      }
      prev = low;
      low = high;
    }
    
    e = Math.log10(low.value());
    if (Math.floor(e) == e) {
      repeats = value / low.value();
      return pattern[0][repeats]
          .replaceAll("\\\\l", low.name());
    } else {
      stepSize = (low.value() - prev.value()) / 4;
      repeats = (value - low.value()) / stepSize;
      return pattern[1][repeats]
          .replaceAll("\\\\l", low.name())
          .replaceAll("\\\\p", prev.name());
    }
  }

  private static List<Integer> breakIntoAdditives(int value) {

    String numeral = Integer.toString(value);
    ArrayList<Integer> additives = new ArrayList<>();

    for (int i = 0; i < numeral.length(); i++) {
      int d = Character.getNumericValue(numeral.charAt(i));
      if (d == 0)
        continue;
      int e = numeral.length() - i - 1;
      int n = d * (int) Math.pow(10, e);
      additives.add(n);
    }
    return additives;
  }

  static int parseRomanNumeral(String numeral) {

    if (numeral == null || numeral.length() == 0) {
      throw new NumberFormatException();
    }

    List<Digit> digits = new ArrayList<>();
    try {
        for(int i=0;i<numeral.length();i++) 
          digits.add(Digit.valueOf(String.valueOf(numeral.charAt(i))));        
    } catch (Exception e) {
      throw new NumberFormatException();
    }

    Digit prevDigit = digits.get(0);
    Digit digit;
    int successiveOccurrences = 1;
    int value = 0;
    String alreadySubtractedDigits = "";

    for (int i = 1; i < digits.size(); i++) {
      digit = digits.get(i);
      successiveOccurrences = digit.equals(prevDigit) ? ++successiveOccurrences : 1;

      // Some digits can repeat but not more than thrice in succession
      if (REPEATABLE_DIGITS.contains(digit.name()) && successiveOccurrences > 3) {
        throw new NumberFormatException(
            digit.name() + "cannot occur more than 3 times in succession");
      }

      // Some digits cannot repeat more than once
      if (NON_REPEATABLE_DIGITS.contains(digit.name())
          && digits.subList(0, i - 1).contains(digit)) {
        throw new NumberFormatException(
            digit.name() + "cannot occur more than once in a Roman numeral");
      }

      // Only some digits are subtracted
      // Only some digits are allowed to be subtracted from some digits
      int compareRatio = prevDigit.compareTo(digit);
      if (compareRatio < 0) {

        if (NON_SUBTRACTED_DIGITS.contains(prevDigit.name()))
          throw new NumberFormatException(prevDigit.name() + "can never be subtracted");

        if (!Pattern.matches(VALID_SUBTRACTIVE_PAIRS, prevDigit.name() + digit.name()))
          throw new NumberFormatException(
              prevDigit.name() + "cannot be subtracted from " + digit.name());

        if (alreadySubtractedDigits.indexOf(prevDigit.name()) > -1)
          throw new NumberFormatException(prevDigit.name() + "already subtracted once");

        alreadySubtractedDigits = alreadySubtractedDigits.concat(digit.name());
        value += - prevDigit.value();

      } else {
        if (compareRatio == 0)
          alreadySubtractedDigits = alreadySubtractedDigits.concat(digit.name());
        value +=  prevDigit.value();
      }
      
      prevDigit = digit;
    }
    value += prevDigit.value();
    return value;
  }

  public List<Digit> getDigits() {
    List<Digit> digits = new ArrayList<>();
    
    for(int i=0;i<numeral.length();i++)
      digits.add(Digit.valueOf(Character.toString(numeral.charAt(i))));  
    return digits;
  }
}
