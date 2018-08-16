package com.codechallenge.intent.termtype;

import java.util.Arrays;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.stream.events.Characters;

import com.codechallenge.intent.Phrase;
import com.codechallenge.intent.Segment;

public class PhraseMatcher implements TermTypeMatcher{

  private Phrase phrase;
  private Segment matchedSegment;
  
  public PhraseMatcher(Phrase phrase) {
    if(phrase==null)
      throw new IllegalArgumentException();
    this.phrase = phrase;
  }
  
  @Override
  public boolean matches(Segment segment) {
    
    String text = phrase.text().replaceAll("\\s+", " ");
    String[] words = text.split(" ");
    String regex;
    
    if(words.length == 1) {
      regex = "\\b" + words[0] + "\\b";
    }else {
      regex   = Arrays.asList(words).stream().collect(Collectors.joining("\\s+","\\b","\\b"));
    }    
                         
    Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
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
  
}
