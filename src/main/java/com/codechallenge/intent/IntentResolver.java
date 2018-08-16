package com.codechallenge.intent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class IntentResolver {

  private List<ExpressionMatchAnalysis> analyses;
  private Sentence sentence;
  private SentenceAnalyser analyser;
  private ExpressionMatchAnalysis analysisOfResolved;
  private State state;
  
  private enum State {
    NOT_STARTED,
    STARTED,
    COMPLETED,
  }

  public static IntentResolver ofSentence(Sentence sentence) {
    return new IntentResolver(sentence);
  }

  private IntentResolver(Sentence sentence) {
    this.sentence = sentence;
    analyses = new ArrayList<>();
    analyser = this.sentence.analyser();
    analysisOfResolved = null;
    this.state = State.NOT_STARTED;
  }

  public void resolve() {

    this.state = State.STARTED;
    Intent.Nature natureOfIntent = guessNatureOfIntent(sentence);    

    List<Expression> expressions = Optional.ofNullable(natureOfIntent)
                                           .map(Expressions::findByNatureOfIntent)
                                           .orElseGet(Expressions::all);    

    ExpressionMatchAnalysis analysis;   
    for(Expression expression : expressions) {  

      analyser.matchWithExpression(expression);
      analysis = analyser.getMatchAnalysis();

      if(analysis.matchesExactly()) {
        analysisOfResolved = analysis;
        this.state = State.COMPLETED;
        return;
      }

      analyses.add(analysis);
    }
    
    this.state = State.COMPLETED;

  }

  private static Intent.Nature guessNatureOfIntent(Sentence sentence) {
    return sentence.isInterrogative() ? Intent.Nature.QUESTION : Intent.Nature.STATEMENT;    
  }

  public String getResolvedIntent() {
    if(state!= State.COMPLETED)
      throw new IllegalStateException();
    
    return Optional.ofNullable(analysisOfResolved)
                   .map(a->a.expression().intent().name())
                   .orElse(null);
  }

  public Map<String,String> getResolvedTerms() {
    if(state!= State.COMPLETED)
      throw new IllegalStateException();
    
    return analysisOfResolved.ofSegmentsMatchedWithTerms().stream()
                             .collect(Collectors.toMap(
                                         analysis->analysis.matchedPart().getAsTerm().name(),
                                         analysis->analysis.segment().toString()));    
  }

}
