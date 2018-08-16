package com.codechallenge.intent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sentence {
  
  private String normalizedText;
  private String actualText;
  private Matcher matcher;
  private static final String REGEX_INTERROGATIVE = "^(?:how|when|who|where|which|what).*\\??$";
  private static final String REGEX_PUNCTUATION = "[\\.!\\?]";
  private static final String SPACE = " ";
  
  public Sentence(String text){
    this.actualText = text;
    this.normalizedText = normalize(text);
    matcher = Pattern.compile(".*").matcher(text);
  }
  
  private String normalize(String text) {
    return text.replaceAll(REGEX_PUNCTUATION, SPACE).trim();    
  }

  public boolean isInterrogative() {
     Pattern pattern = Pattern.compile(REGEX_INTERROGATIVE,Pattern.CASE_INSENSITIVE);
     return pattern.matcher(actualText).matches();
  }
  
  public int length() {
    return normalizedText.length();
  }
  
  public Segment toSegment() {
    return new Segment(this,0,length()-1);
  }
  
  public String toString() {
    return normalizedText;
  }
  
  public String actualText() {
    return actualText;
  }
  
  public Matcher matcher() {
    return matcher;
  }
  
  public SentenceAnalyser analyser() {
    return new SentenceAnalyser(this);
  }
}
