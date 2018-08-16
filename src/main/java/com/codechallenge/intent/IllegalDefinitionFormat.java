package com.codechallenge.intent;

public class IllegalDefinitionFormat extends RuntimeException {
  
  public IllegalDefinitionFormat() {
    super();
  }
  
  public IllegalDefinitionFormat(String message) {
    super(message);
  }
  
  public IllegalDefinitionFormat(Throwable cause) {
    super(cause);
  }

}
