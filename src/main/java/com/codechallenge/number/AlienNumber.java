package com.codechallenge.number;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AlienNumber {

  private static Map<String,RomanNumber.Digit> symbols = new HashMap<>();
  
  private int value;
  private String numeral;
    
  public AlienNumber(int value) {
    this.numeral = convertToAlienNumeral(value);
    this.value   = value;
  }
  
  public AlienNumber(String aNumeral){
    String numeral = sanitize(aNumeral);
    this.value = parseAlienNumeral(numeral);
    this.numeral = numeral;
  }
  
  public int value() {
    return value;
  }
  
  public String toString() {
    return numeral;
  }
  
  public static List<String> symbols() {
    return symbols.keySet().stream().collect(Collectors.toList());
  }
  
  public static boolean mapToRomanSymbol(String anAlienSymbol, String aRomanSymbol) {
     
     String alienSymbol = anAlienSymbol.trim().toLowerCase();
     String romanSymbol = aRomanSymbol.trim().toUpperCase();
     RomanNumber.Digit identifiedRomanSymbol;
     
     if(alienSymbol.contains(" "))
        throw new IllegalArgumentException(
            "\""+alienSymbol+"\" is not a single word");
     
     if((identifiedRomanSymbol = symbols.get(alienSymbol)) != null) {
       if(identifiedRomanSymbol.toString() != romanSymbol)
         throw new IllegalArgumentException(
             "\""+anAlienSymbol+"\" is already mapped to "+identifiedRomanSymbol.toString());
       else return false; //Already mapping exists
     }     
     
     try {
       identifiedRomanSymbol = RomanNumber.Digit.valueOf(RomanNumber.Digit.class,romanSymbol);
     }
     catch(RuntimeException e) {
       throw new IllegalArgumentException("\""+aRomanSymbol+"\" is not a roman symbol");
     }
     
     if(symbols.containsValue(identifiedRomanSymbol))
       throw new IllegalArgumentException(
            "\"" + aRomanSymbol + "\" is already mapped to " + "\""+alienSymbol+"\"");
     
     
     symbols.put(alienSymbol,identifiedRomanSymbol);
     return true;
  }
  
  private static int parseAlienNumeral(String numeral) {

    List<String> digits = Arrays.asList(numeral.split("\\s+"));
    
    String romanNumeral = "";
    String romanDigit;
    for(String digit : digits) {
      romanDigit = Optional.ofNullable(symbols.get(digit))
                           .map(RomanNumber.Digit::name)
                           .orElseThrow(()-> new NumberFormatException(
                               digit + "is not known"));
      
      romanNumeral = romanNumeral.concat(romanDigit);
    }
    return RomanNumber.parseRomanNumeral(romanNumeral);
  }
  
  private static String convertToAlienNumeral(int value) {

    RomanNumber romanNumber = new RomanNumber(value);
    List<RomanNumber.Digit> digits = romanNumber.getDigits();
    String numeral = "";
    
    for(RomanNumber.Digit digit : digits) {
      Optional<String> alienSymbol = symbolFor(digit);
      if(alienSymbol.isPresent())
        throw new IllegalArgumentException(digit.name() + "is not mapped to any alien symbol");
      numeral = String.join(" ", numeral,alienSymbol.get());      
    }
    return numeral;      
  }  

  private static String sanitize(String numeral) {
    return numeral.trim().replace("\\s+"," ").toLowerCase();
  }
  
  private static Optional<String> symbolFor(RomanNumber.Digit romanSymbol) {
    return symbols.entrySet().stream()
                  .filter(e->e.getValue().equals(romanSymbol))
                  .findFirst()
                  .map(e->e.getKey());
  }

}
