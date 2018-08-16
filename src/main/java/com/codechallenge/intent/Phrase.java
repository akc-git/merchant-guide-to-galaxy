package com.codechallenge.intent;

public class Phrase implements ExpressionPart{

  private String text;
  
  Phrase(String text){
    this.text = text;
  }
  
  public String text() {
    return text;
  }
  
  public boolean isPhrase() {
    return true;
  }

  public boolean isTerm() {
    return false;
  }

  public Phrase getAsPhrase() {
      return this;
  }

  public Term getAsTerm() {
    return null;
  }
  
  public boolean equals(Object obj) {
    if(!(obj instanceof Phrase))
       return false;
    Phrase other = (Phrase)obj;
    return this.text.equals(other.text);
  }

}
