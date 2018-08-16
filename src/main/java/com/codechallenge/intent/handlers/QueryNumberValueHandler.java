package com.codechallenge.intent.handlers;

import java.util.Optional;

import com.codechallenge.intent.Handler;
import com.codechallenge.intent.IntentRequest;
import com.codechallenge.intent.api.IntentHandler;
import com.codechallenge.number.AlienNumber;

@Handler(forIntent="QueryNumberValue")
public class QueryNumberValueHandler implements IntentHandler {

  @Override
  public void processRequest(IntentRequest request) { 
    try {

      String valueOfTerm = Optional.ofNullable(request.valueOfTerm("alien-numeral"))
          .orElse(null);

      int value = Optional.ofNullable(valueOfTerm)
                          .map(AlienNumber::new)
                          .map(AlienNumber::value)
                          .orElse(null);

      request.response().inform(valueOfTerm + " is " + value );

    }catch(Exception e) {
      request.response().inform(e.getMessage());  
    }
  }

}
