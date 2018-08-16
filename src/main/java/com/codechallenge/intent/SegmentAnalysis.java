package com.codechallenge.intent;

public class SegmentAnalysis {
  
  private Segment segment;
  private boolean matched;
  private ExpressionPart matchedPart;
  
  public SegmentAnalysis(Segment segment) {
    this.segment = segment;
    this.matched = false;
    this.matchedPart = null;
  }
 
  public boolean matched() {
    return matched;
  }
  
  public Segment segment() {
    return segment;
  }
  
  public ExpressionPart matchedPart(){
    return matchedPart;
  }
  
  public void setMatchedPart(ExpressionPart part) {
    this.matchedPart = part;
    this.matched = true;
  }
  
  public boolean equals(Object obj) {
    if(!(obj instanceof SegmentAnalysis))
      return false;
    SegmentAnalysis other = (SegmentAnalysis) obj;
    return this.segment.equals(other.segment());
  }
  
}