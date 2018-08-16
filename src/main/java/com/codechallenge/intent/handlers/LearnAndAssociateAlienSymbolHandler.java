package com.codechallenge.intent.handlers;

import com.codechallenge.intent.Handler;
import com.codechallenge.intent.IntentRequest;
import com.codechallenge.intent.api.IntentHandler;
import com.codechallenge.number.AlienNumber;

@Handler(forIntent="LearnAndAssociateAlienSymbol")
public class LearnAndAssociateAlienSymbolHandler implements IntentHandler {

  @Override
  public void processRequest(IntentRequest request) {

    String alienSymbol = request.valueOfTerm("alien-symbol");
    String romanSymbol = request.valueOfTerm("roman-symbol");

    try {
      AlienNumber.mapToRomanSymbol(alienSymbol, romanSymbol);
    }
    catch(IllegalArgumentException e) {
      request.response().inform(e.getMessage());
      return;
    }
    
    request.response().inform("OK! noted");

  }

}
