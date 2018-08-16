package com.codechallenge.intent;

public class IntentResponse {
  
  public enum Type {
    INFORM,
    CONFIRM
  }
  
  private IntentRequest respondedRequest;
  private Type responseType;
  private String reply;
  
  public IntentResponse(IntentRequest toRequest) {
    this.respondedRequest = toRequest;
  }
  
  public void inform(String reply) {
    this.responseType = Type.INFORM;
    this.reply   = reply;
  }
  
  public void confirm(String reply) {
    this.responseType = Type.CONFIRM;
    this.reply        = reply;
  }
  
  public Type responseType() {
    return this.responseType;
  }
  
  public String toString() {
    return reply;
  }
  
  public IntentRequest respondedRequest() {
    return respondedRequest;
  }

}
