package com.codechallenge.intent;

public interface ExpressionPart {
  
  boolean isPhrase();
  boolean isTerm();
  Phrase getAsPhrase();
  Term getAsTerm();
  
  enum Type{
    PHRASE,
    TERM
  }
}
