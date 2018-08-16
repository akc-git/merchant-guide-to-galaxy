package com.codechallenge.intent;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class ExpressionMatchAnalysis {

  private Expression expression;
  private List<SegmentAnalysis> segments;
  private List<ExpressionPart> missingParts;

  public ExpressionMatchAnalysis(Sentence sentence,Expression expression) {
    this.expression   = expression;
    segments     = new ArrayList<>();
    missingParts = new ArrayList<>();

    Segment segment = sentence.toSegment();
    segments.add(new SegmentAnalysis(segment));
  }

  public void record(SegmentAnalysis segmentAnalysis) {
    
    if(segments.isEmpty()) {
      segments.add(segmentAnalysis);
      return;
    }
    
    Segment segment;
    Segment matchedSegment = segmentAnalysis.segment();
    
    for(int index=0;index < segments.size(); index++) {
      
        if(segments.get(index).matched()) 
           continue;

        segment = segments.get(index).segment();
        
        if(segment.equals(matchedSegment)) {
          segments.set(index, segmentAnalysis);
          return;
        }
          
        List<Segment> splits = segment.split(matchedSegment);
        if(!splits.isEmpty()) {

          segments.remove(index);
          for(Segment split : splits) {
            if(split.equals(matchedSegment))
              segments.add(index, segmentAnalysis);
            else
              segments.add(index, new SegmentAnalysis(split));

            index++;              
          }
          return;
        }          
    }    

  }

  public Expression expression() {
    return this.expression;
  }
  
  public List<ExpressionPart> missingParts(){
    return this.missingParts;
  }

  public SegmentAnalysis ofFirstUnmatched() {
    return segments.stream()
                   .filter(analysis->!analysis.matched())
                   .findFirst()
                   .orElse(null);
  }

  public Segment ofSegmentMatchedWith(ExpressionPart part) {
    return segments.stream()
                   .filter(analysis-> {
                      return analysis.matched() &&
                             analysis.matchedPart().equals(part);
                     }).findFirst()
                       .map(SegmentAnalysis::segment)
                       .orElse(null);                  
  }

  public SegmentAnalysis ofFirstUnmatchedAfter(Segment segment) {
    
    ListIterator<SegmentAnalysis> iterator;
    SegmentAnalysis segmentAnalysis;
    iterator = segments.listIterator(indexOf(segment));
    
    while(iterator.hasNext()) {
       segmentAnalysis = iterator.next();
       if(!segmentAnalysis.matched())
         return segmentAnalysis;
    }
    
    return null;

  }

  public SegmentAnalysis ofFirstUnmatchedBefore(Segment segment) {

    ListIterator<SegmentAnalysis> iterator;
    SegmentAnalysis segmentAnalysis;
    iterator = segments.listIterator(indexOf(segment));
    
    while(iterator.hasPrevious()) {
       segmentAnalysis = iterator.previous();
         return segmentAnalysis;
    }
    
    return null;
  }

  public int indexOf(Segment segment) {
    for(int index = 0; index < segments.size();index++) {
      if(segments.get(index).segment().equals(segment))
        return index;
    }
    return -1;
  }

  public List<SegmentAnalysis> ofSegmentsMatchedWithTerms() {
     return segments.stream()
                    .filter(s->s.matched() && s.matchedPart().isTerm())
                    .collect(Collectors.toList());
  }

  public boolean matchesExactly() {     
    return missingParts.isEmpty() && 
           !containsUnmatchedSegments();
  }
  
  public boolean containsUnmatchedSegments() {
    return segments.stream().anyMatch(s->!s.matched());
  }

} 

