package com.codechallenge.intent.termtype;

import java.util.regex.Pattern;

@TermMatcher(termType=TermType.Name.DECIMAL_NUMERAL)
public class DecimalNumeralMatcher extends AbstractTermTypeMatcher{

  private static Pattern pattern;

  @Override
  protected Pattern getPattern() {
    if(pattern==null) {
      String regex = "(?:\\+|-)?[0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+";
      pattern = Pattern.compile(regex);
    }
    return pattern;
  }
}
