package com.codechallenge.intent.termtype;

import com.codechallenge.intent.Segment;

public interface TermTypeMatcher {
  
  boolean matches(Segment segment);
  Segment getMatchedSegment();
  
}
