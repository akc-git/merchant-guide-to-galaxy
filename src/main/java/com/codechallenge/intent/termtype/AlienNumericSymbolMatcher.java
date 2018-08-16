package com.codechallenge.intent.termtype;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.codechallenge.number.AlienNumber;

@TermMatcher(termType=TermType.Name.ALIEN_NUMERIC_SYMBOL)
public class AlienNumericSymbolMatcher extends AbstractTermTypeMatcher {

  @Override
  protected Pattern getPattern() {

    String regex =  AlienNumber.symbols().stream()
                               .collect(Collectors.joining("|"));

    return Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
  }

}
