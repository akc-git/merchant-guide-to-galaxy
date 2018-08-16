package com.codechallenge.intent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Expression {

  private Intent intent;
  private List<ExpressionPart> parts = new ArrayList<>();

  private Expression() {
  }

  private Expression(Intent intent, List<ExpressionPart> parts) {
    this.intent = intent;
    this.parts  = parts;
  }
  
  public Intent intent() {
    return this.intent;
  }
  
  public List<ExpressionPart> parts() {
    return parts;
  }
  
  public List<ExpressionPart> parts(ExpressionPart fromPart) {
    return parts.subList(parts.indexOf(fromPart), parts.size());
  }
  
  public List<Phrase> phrases() {
    return parts.stream()
                .filter(ExpressionPart::isPhrase)
                .map(ExpressionPart::getAsPhrase)
                .collect(Collectors.toList());
  }
  
  public List<Term> terms() {
    return parts.stream()
                .filter(ExpressionPart::isTerm)
                .map(ExpressionPart::getAsTerm)
                .collect(Collectors.toList());
  }

  public static ExpressionBuilder newOne() {
    return new ExpressionBuilder();
  }

  public static class ExpressionBuilder {

    private Intent intent;
    private List<ExpressionPart> parts = new ArrayList<>();

    private ExpressionBuilder() {}
    
    public ExpressionBuilder ofIntent(Intent intent) {
      this.intent = intent;
      return this;
    }
    
    public ExpressionBuilder beginsWithPhrase(String phrase) {
      if (!parts.isEmpty())
        throw new IllegalStateException();

      ExpressionPart part = new Phrase(phrase);
      parts.add(part);
      return this;
    }

    public ExpressionBuilder beginsWithTerm(Term term) {
      if (!parts.isEmpty())
        throw new IllegalStateException();

      parts.add(term);
      return this;
    }

    public ExpressionBuilder followedByPhrase(String phrase) {
      if (parts.isEmpty())
        throw new IllegalStateException();

      ExpressionPart part = new Phrase(phrase);
      parts.add(part);
      return this;
    }

    public ExpressionBuilder followedByTerm(Term term) {
      if (parts.isEmpty())
        throw new IllegalStateException();

      parts.add(term);
      return this;
    }

   public Expression create() {
     return new Expression(intent,parts);
   }

  }

  public int indexOf(ExpressionPart part) {
    return parts.indexOf(part);
  }

}
