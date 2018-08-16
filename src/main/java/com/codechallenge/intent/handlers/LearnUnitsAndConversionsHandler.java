package com.codechallenge.intent.handlers;

import java.util.Optional;

import com.codechallenge.intent.Handler;
import com.codechallenge.intent.IntentRequest;
import com.codechallenge.intent.api.IntentHandler;
import com.codechallenge.number.AlienNumber;
import com.codechallenge.units.FactorOfConversion;
import com.codechallenge.units.Unit;

@Handler(forIntent="LearnUnitsAndConversions")
public class LearnUnitsAndConversionsHandler implements IntentHandler {

  @Override
  public void processRequest(IntentRequest request) {

    int qtyOfUnit1;
    try {
      qtyOfUnit1 = Optional.ofNullable(request.valueOfTerm("qty-of-unit1"))
                           .map(AlienNumber::new)
                           .map(AlienNumber::value)
                           .orElse(null);

      Unit unit1;
      unit1 = Optional.ofNullable(request.valueOfTerm("unit1"))
                      .map(Unit::create)
                      .orElse(null);


      int qtyOfUnit2 = Optional.ofNullable(request.valueOfTerm("qty-of-unit2"))
                               .map(Integer::parseInt)
                               .orElse(null);

      Unit unit2;
      unit2 = Optional.ofNullable(request.valueOfTerm("unit2"))
          .map(Unit::create)
          .orElse(null);

      FactorOfConversion factor = new FactorOfConversion(qtyOfUnit2,qtyOfUnit1);
      Unit.createFactorOfConversion(unit1, unit2, factor);
      
      request.response().inform("Ok! noted");

    }catch(NumberFormatException e) {
      request.response().inform(e.getMessage());
    }    

  }

}
