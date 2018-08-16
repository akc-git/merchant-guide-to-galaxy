package com.codechallenge.intent.api;

import com.codechallenge.intent.IntentRequest;

public interface IntentHandler {
  
  void processRequest(IntentRequest request);
  
}
