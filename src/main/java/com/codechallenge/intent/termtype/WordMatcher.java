package com.codechallenge.intent.termtype;

import java.util.regex.Pattern;

@TermMatcher(termType=TermType.Name.WORD)
public class WordMatcher extends AbstractTermTypeMatcher {
  
  private static Pattern pattern;

  protected Pattern getPattern() {
    if(pattern==null) {
      pattern = Pattern.compile("[\\w-]+",Pattern.CASE_INSENSITIVE);
    }
    return pattern;
  }

}
