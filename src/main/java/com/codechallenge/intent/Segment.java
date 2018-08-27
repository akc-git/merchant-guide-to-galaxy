package com.codechallenge.intent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class Segment {
  
  private Sentence sentence;
  private boolean imaginary;
  private int from;
  private int to;
  
  public Segment(Sentence sentence, int from, int to) {
    if(from < 0 || from >=  sentence.length() ||
         to < 0 || to >= sentence.length() ||
         from > to)
      throw new IllegalArgumentException();
      
    this.sentence = sentence;
    this.from = from;
    this.to   = to;
    this.imaginary = false;
  }
  
  public int start() {
    return from;
  }
  
  public int end() {
    return to;
  }
  
  public String toString() {
    return sentence.toString().substring(from,to+1);
  }
  
  public boolean ofSentence(Sentence sentence) {
    return this.sentence == sentence;
  }
  
  public boolean overlapsWith(Segment aSegment) {
    if(!aSegment.ofSentence(this.sentence))
      return false;
    
    if(this.from < aSegment.end() || this.to < aSegment.start())
       return false;
    else    
      return true;    
  }
  
  public Matcher matcher() {
    Matcher matcher = sentence.matcher();
    matcher.region(from, to+1);
    return matcher;
  }
  
  public Segment subSegment(int from, int to) {
    if(from > to || from > end() || to < start() )
      throw new IllegalArgumentException();
    return new Segment(sentence, from, to);
  }
  
  public List<Segment> split(Segment that){
    
    List<Segment> splits = new ArrayList<>();
    if(!this.contains(that)) 
      return splits;
    
    if(this.start() == that.start()) {
       splits.add(that.trim());
       splits.add(subSegment(that.end()+1,this.end()).trim());
    }else if(this.end() == that.end()) {
      splits.add(subSegment(this.start(),that.start()-1).trim());
      splits.add(that);
    }else {
      splits.add(subSegment(this.start(),that.start()-1).trim());
      splits.add(that);
      splits.add(subSegment(that.end()+1,this.end()).trim());      
    }
    
    return splits;    
  }
  
  private boolean contains(Segment that) {
    if(!this.ofSentence(that.sentence))
      return false;
    
    if(this.equals(that))
       return false;
    
    return (this.start() <= that.start() && 
            this.end() >= that.end());
  }

  public boolean equals(Object obj) {
    if(!(obj instanceof Segment))
      return false;
    
    Segment other = (Segment) obj;
    return (this.sentence.equals(other.sentence) &&
            this.imaginary == other.imaginary &&
            this.from == other.from &&
            this.to == other.to);
  }

  public Segment trim() {
    String text = sentence.toString();
    
    while(from <= to && Character.isWhitespace(text.charAt(from)))
        from++;
    while(from<=to && Character.isWhitespace(text.charAt(to)))
        to--;

    return this;
  }

  public int length() {
    return to - from + 1;
  }

}
