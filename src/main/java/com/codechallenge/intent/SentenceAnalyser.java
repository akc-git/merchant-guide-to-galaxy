package com.codechallenge.intent;

import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import com.codechallenge.intent.termtype.PhraseMatcher;
import com.codechallenge.intent.termtype.TermType;
import com.codechallenge.intent.termtype.TermTypeMatcher;

public class SentenceAnalyser {

  private ExpressionMatchAnalysis analysis;
  private Sentence sentence;

  public SentenceAnalyser(Sentence sentence) {
    this.sentence = sentence;
    this.analysis = null;
  }

  public void matchWithExpression(Expression expression) {    

    analysis = new ExpressionMatchAnalysis(sentence,expression);

    matchPhrasesIn(expression);
    matchTermsIn(expression);
    
    analysis.complete();
  }
  
  public ExpressionMatchAnalysis getMatchAnalysis() {
    return this.analysis;
  }

  private void matchTermsIn(Expression expression) {

    ExpressionPart nearestPart;
    Segment segment;
    Segment refSegment;
    SegmentAnalysis segmentAnalysis;
    TermTypeMatcher matcher;
    
    int indexOfTerm;
    int indexOfNearestPart;
    
    List<Term> terms = expression.terms();
    
    for(Term term : terms) {

      
      nearestPart = findNearestMatchedPart(term);
      if(nearestPart==null) {
        segment = Optional.ofNullable(analysis.ofFirstUnmatched())
                          .map(SegmentAnalysis::segment)
                          .orElse(null);
      } else {
        
        indexOfTerm        = expression.indexOf(term);
        indexOfNearestPart = expression.indexOf(nearestPart);
        refSegment         = analysis.ofSegmentMatchedWith(nearestPart);
        
        if(indexOfNearestPart < indexOfTerm) {
          segment = Optional.ofNullable(analysis.ofFirstUnmatchedAfter(refSegment))
                            .map(SegmentAnalysis::segment)
                            .orElse(null);
        }else {
          segment = Optional.ofNullable(analysis.ofFirstUnmatchedBefore(refSegment))
                            .map(SegmentAnalysis::segment)
                            .orElse(null);
        } 
        
      }

      if(segment==null) {
        /*Consider rest of the terms including this one as missing parts */
        expression.parts(term).stream()
                  .filter(ExpressionPart::isTerm)
                  .forEach(part -> analysis.missingParts().add(part));
        return;
      }

      try {
        
        matcher = TermType.getMatcher(term.termType());
        
      } catch (InstantiationException | IllegalAccessException e) {
        throw new IllegalStateException(e);
      }

      if(!matcher.matches(segment)) {
        analysis.missingParts().add(term);

      }else {
        segmentAnalysis = new SegmentAnalysis(matcher.getMatchedSegment());
        segmentAnalysis.setMatchedPart(term);
        analysis.record(segmentAnalysis);        
      }    
    }
  }

  private ExpressionPart findNearestMatchedPart(Term term) {

    ListIterator<ExpressionPart> iterator; 
    ExpressionPart part;
    Boolean matched;
    int indexOfTerm = analysis.expression().indexOf(term);
    int indexOfLastPart = analysis.expression().parts().size()-1;
    
    iterator = analysis.expression().parts().listIterator(indexOfTerm);
    while(iterator.hasPrevious()) {
      part = iterator.previous();
      matched  = Optional.ofNullable(analysis.ofSegmentMatchedWith(part))
                         .isPresent();
      if(matched)
        return part;      
    }

    if(indexOfTerm==indexOfLastPart) {
      return null;
    }
    
    iterator = analysis.expression().parts().listIterator(indexOfTerm+1);
    while(iterator.hasNext()) {
      part = iterator.next();
      matched  = Optional.ofNullable(analysis.ofSegmentMatchedWith(part))
                         .isPresent();
      if(matched)
        return part;
    }

    return null;
  }

  private void matchPhrasesIn(Expression expression) {

    Segment segment;
    SegmentAnalysis segmentAnalysis;
    TermTypeMatcher matcher; 

    List<Phrase> phrases = expression.phrases();

    for(Phrase phrase : phrases) {

      segment = Optional.ofNullable(analysis.ofFirstUnmatched())
                        .map(SegmentAnalysis::segment)
                        .orElse(null);

      if(segment==null) {
        /*Consider rest of the phrases including this one as missing parts */
        expression.parts(phrase).stream()
                  .filter(ExpressionPart::isPhrase)
                  .forEach(part -> analysis.missingParts().add(part));
        return;
      }

      matcher = new PhraseMatcher(phrase);
      if(!matcher.matches(segment)) {
        analysis.missingParts().add(phrase);

      }else {
        segmentAnalysis = new SegmentAnalysis(matcher.getMatchedSegment());
        segmentAnalysis.setMatchedPart(phrase);
        analysis.record(segmentAnalysis);        
      }        

    }

  }

}

