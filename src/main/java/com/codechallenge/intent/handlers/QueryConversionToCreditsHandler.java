package com.codechallenge.intent.handlers;

import java.util.Optional;

import com.codechallenge.intent.Handler;
import com.codechallenge.intent.IntentRequest;
import com.codechallenge.intent.api.IntentHandler;
import com.codechallenge.number.AlienNumber;
import com.codechallenge.units.FactorOfConversion;
import com.codechallenge.units.Unit;

@Handler(forIntent="QueryConversionToCredits")
public class QueryConversionToCreditsHandler implements IntentHandler {

  @Override
  public void processRequest(IntentRequest request) {
    try {

      AlienNumber qtyFromUnit = Optional.ofNullable(request.valueOfTerm("qty"))
                                .map(AlienNumber::new)
                                .orElse(null);

      Unit fromUnit = Optional.ofNullable(request.valueOfTerm("from-unit"))
                              .map(Unit::get)
                              .orElse(null);

      Unit toUnit = Optional.ofNullable(request.valueOfTerm("to-unit"))
                            .map(Unit::get)
                            .orElse(null);

      if(!fromUnit.convertibleTo(toUnit)) {
        request.response().inform(
            String.format("I don't know how to convert from %s to %s",fromUnit.symbol(),toUnit.symbol()));
        return;
      }
      
      FactorOfConversion foc = Unit.getFactorOfConversion(fromUnit, toUnit);
      float qtyToUnit = qtyFromUnit.value() * foc.numerator() / foc.denominator();
        
      request.response().inform(
          String.format("%s %s is %d %s",qtyFromUnit.toString(),fromUnit.symbol(),
                          qtyToUnit, toUnit.symbol()));

    }catch(Exception e) {
      request.response().inform(e.getMessage());  
    }

  }
}
