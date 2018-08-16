package com.codechallenge.intent.api;

import com.codechallenge.intent.IntentRequest;
import com.codechallenge.intent.Sentence;

public interface IntentMatcher {

  void match(Sentence sentence);
  boolean matches();
  IntentRequest getIntentRequest();
  
}
