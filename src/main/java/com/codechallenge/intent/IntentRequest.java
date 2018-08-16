package com.codechallenge.intent;

import java.util.Map;

public class IntentRequest {
  
  private Sentence sentence;
  private String nameOfIntent;
  private Map<String,String> resolvedTerms;
  private IntentResponse response;
  
  public IntentRequest() {
    this.response = new IntentResponse(this);
  }

  public Sentence sentence() {
    return sentence;
  }

  public String nameOfIntent() {
    return nameOfIntent;
  }

  public Map<String, String> resolvedTerms() {
    return resolvedTerms;
  }

  public IntentResponse response() {
    return response;
  }
  
  public String valueOfTerm(String termName) {
    return resolvedTerms.get(termName);
  }

  public void setSentence(Sentence sentence) {
    this.sentence = sentence;
  }

  public void setNameOfIntent(String nameOfIntent) {
    this.nameOfIntent = nameOfIntent;
  }

  public void setResolvedTerms(Map<String, String> resolvedTerms) {
    this.resolvedTerms = resolvedTerms;
  }
  
}
