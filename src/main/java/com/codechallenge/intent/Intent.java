package com.codechallenge.intent;

import java.util.List;

import com.codechallenge.intent.api.IntentHandler;

public class Intent{
  
  public enum Nature {
    STATEMENT,
    QUESTION,
    COMMAND
  }
  
  private String name;
  private Nature nature;
  private List<Term> terms;    

  public String name() {
    return name;
  }

  public Nature nature() {
    return nature;
  }

  public IntentHandler handler() {
    
    Class<?> claz = Intents.getTypeOfHandler(this.name);
    if(claz==null)
       return null;
      
    try {
      return (IntentHandler) claz.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      return null;
    }
    
  }
  
  public boolean hasTerm(String termName) {
    return terms.stream()
                .anyMatch(term->term.name().equals(termName));
  }
  
  public Term getTerm(String termName) {
    return terms.stream()
                .filter(term->term.name()
                .equals(termName))
                .findFirst().orElse(null);
  }
  
  public boolean equals(Object obj) {
    if(!(obj instanceof Intent))
      return false;
    Intent other = (Intent) obj;
    return this.name.equals(other.name);
  }
  
  public static IntentBuilder newIntent() {
    return new IntentBuilder();
  }
  
  public static class IntentBuilder {
    
    private String name;
    private Nature nature;
    private List<Term> terms;
    
    private IntentBuilder() {}
    
    public IntentBuilder withName(String name) {
      this.name = name;
      return this;
    }
    
    public IntentBuilder ofNature(Nature nature) {
      this.nature = nature;
      return this;
    }
    
    public IntentBuilder havingTerms(List<Term> terms) {
      this.terms = terms;
      return this;
    }
   
    public Intent create() {
      Intent intent = new Intent();
      intent.name = name;
      intent.nature = nature;
      intent.terms  = terms;
      return intent;
    }
    
  }

}
