package com.codechallenge.intent.termtype;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.codechallenge.units.Unit;

@TermMatcher(termType=TermType.Name.UNIT)
public class UnitMatcher extends AbstractTermTypeMatcher {

  @Override
  protected Pattern getPattern() {
    String regex = Unit.getAll().stream()
                       .map(Unit::symbol)
                       .collect(Collectors.joining("|"));

    if(regex==null||regex.isEmpty())
      return null;
    
    return  Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
  }
}
