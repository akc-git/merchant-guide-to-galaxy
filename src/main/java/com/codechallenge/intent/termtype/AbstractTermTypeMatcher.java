package com.codechallenge.intent.termtype;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codechallenge.intent.Segment;

public abstract class AbstractTermTypeMatcher implements TermTypeMatcher {

  protected Segment matchedSegment;
  
  @Override
  public boolean matches(Segment segment) {
    
    Pattern pattern = getPattern();    
    if(pattern==null)
      return false;
    
    Matcher matcher = segment.matcher().usePattern(pattern);
    
    if(matcher.find()) {
      MatchResult result = matcher.toMatchResult();
      matchedSegment = segment.subSegment(result.start(), result.end()-1);      
      return true;
    }
    else {
      matchedSegment = null;
      return false;      
    }   
  }

  @Override
  public Segment getMatchedSegment() {
    return matchedSegment;
  }
  
  protected abstract Pattern getPattern(); 

}
