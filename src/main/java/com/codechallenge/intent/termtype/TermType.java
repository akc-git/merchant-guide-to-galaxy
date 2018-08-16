package com.codechallenge.intent.termtype;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.reflections.Reflections;

public class TermType {

  public enum Name {
    ALIEN_NUMERIC_SYMBOL,
    ROMAN_NUMERIC_SYMBOL,
    ROMAN_NUMERAL,
    ALIEN_NUMERAL,
    DECIMAL_NUMERAL,
    UNIT,
    WORD;  
  }
  
  private static Map<Name,Class<TermTypeMatcher>> matcherLookup = new EnumMap<>(Name.class);
  
  private TermType() {}
  
  public static void scanMatchers() {
    
    Reflections reflections = new Reflections("com.codechallenge");
    Set<Class<?>> scannedMatchers = reflections.getTypesAnnotatedWith(TermMatcher.class);
    
    for(Class<?> claz : scannedMatchers) {
      if(!claz.isAnnotationPresent(TermMatcher.class))
        continue;
      TermMatcher annotation = claz.getAnnotation(TermMatcher.class);
      Name name = annotation.termType();
      matcherLookup.putIfAbsent(name, (Class<TermTypeMatcher>) claz);
    }
    
    //Check that there are matchers implemented for each TermType
    Arrays.asList(Name.values()).stream().forEach(name -> 
      Optional.ofNullable(matcherLookup.get(name))
          .orElseThrow(IllegalStateException::new));
  
  }

  public static TermTypeMatcher getMatcher(Name name) throws InstantiationException, IllegalAccessException {
    Class<TermTypeMatcher> claz = matcherLookup.get(name);
    return claz.newInstance();
  }  
  
}
