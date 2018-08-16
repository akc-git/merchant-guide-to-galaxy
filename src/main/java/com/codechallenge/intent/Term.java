package com.codechallenge.intent;

import com.codechallenge.intent.termtype.TermType;

public class Term implements ExpressionPart{
  
  private String name;
  private TermType.Name termType;
  private static String VALID_TERM_NAME_REGEX = "^[\\w-]+$";
  
  private Term() {}
  
  Term(String name, TermType.Name termType){
     this();
     this.name     = parseName(name);
     this.termType = termType;     
  }
  
  public String name() {
    return this.name;
  }
  
  public TermType.Name termType() {
    return this.termType;
  }
  
  @Override
  public boolean isPhrase() {
    return false;
  }

  @Override
  public boolean isTerm() {
    return true;
  }

  @Override
  public Phrase getAsPhrase() {
    return null;
  }

  @Override
  public Term getAsTerm() {
    return this;
  }
  
  public boolean equals(Object obj) {
    if(!(obj instanceof Term))
      return false;
    Term other = (Term)obj;
    return this.name.equals(other.name);
  }
  
  private static String parseName(String name) {
    String termName = name.toLowerCase().trim();
    if(!termName.matches(VALID_TERM_NAME_REGEX))
       throw new IllegalArgumentException();
    return termName;
  }  
  
}
