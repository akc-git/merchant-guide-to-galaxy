package com.codechallenge.intent.termtype;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.codechallenge.number.AlienNumber;

@TermMatcher(termType=TermType.Name.ALIEN_NUMERAL)
public class AlienNumeralMatcher extends AbstractTermTypeMatcher {

  @Override
  protected Pattern getPattern() {
    // (?:glob|pish|tegj|prok)(?:\s+(?:glob|pish|tegj|prok))*
    String regex = AlienNumber.symbols().stream()
                              .collect(Collectors.joining("|"));    
    
    if(regex==null||regex.isEmpty())
      return null;
    
    regex = "(?:" + regex + ")(?:\\s+(?:" + regex + "))*"; 
    return Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
  }

}
