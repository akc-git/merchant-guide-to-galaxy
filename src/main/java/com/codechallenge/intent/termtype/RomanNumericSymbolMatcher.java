package com.codechallenge.intent.termtype;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.codechallenge.number.RomanNumber;

@TermMatcher(termType=TermType.Name.ROMAN_NUMERIC_SYMBOL)
public class RomanNumericSymbolMatcher extends AbstractTermTypeMatcher {

  private static Pattern pattern;
  
  @Override
  protected Pattern getPattern() {
    if(pattern==null) {
      String regex = Arrays.asList(RomanNumber.Digit.values()).stream()
                           .map(RomanNumber.Digit::name)
                           .collect(Collectors.joining());
      regex = "[" + regex + "]";
      pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
    }
    return pattern;
  }

}
