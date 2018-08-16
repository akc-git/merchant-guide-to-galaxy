package com.codechallenge.units;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javafx.util.Pair;

public class Unit {
  
  private static List<Unit> units = new ArrayList<>();
  private static Map<Pair<Unit,Unit>,FactorOfConversion> focs = new HashMap<>();
  
  private String symbol;
  
  private Unit() {}
  
  private Unit(String symbol) {
    this.symbol = symbol;
  }  

  public String symbol() {
    return symbol;
  }
  
  public boolean convertibleTo(Unit toUnit) {
    return Optional.ofNullable(getFactorOfConversion(this,toUnit))
                   .isPresent();
  }
  
  @Override
  public boolean equals(Object object) {
    if (object instanceof Unit) {
      return symbol == ((Unit) object).symbol();
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    return symbol.hashCode();
  }
  
  public static Unit create(String aSymbol) {
    
    Unit unit;    
    if(aSymbol.isEmpty())
       throw new IllegalArgumentException("symbol for unit cannot be blank");
    
    String symbol = normalize(aSymbol);
    if(symbol.contains(" "))
      throw new IllegalArgumentException("symbol for unit is not a single word");
    
    if((unit = get(symbol))==null) {
        unit = new Unit(symbol);
        units.add(unit);
    }
    
    return unit;
  }
  
  public static List<Unit> getAll(){
    return units;
  }
  
  public static Unit get(String aSymbol) {
    String symbol = normalize(aSymbol);
    return units.stream()
          .filter(unit->unit.symbol().equals(symbol))
          .findFirst()
          .orElse(null);
  }
  
  public static FactorOfConversion getFactorOfConversion(Unit fromUnit, Unit toUnit) {
    if(fromUnit==null||toUnit==null)
      throw new IllegalArgumentException();
    
    if(fromUnit.equals(toUnit))
      return FactorOfConversion.UNITY;    
    
    Pair<Unit, Unit> forPair = new Pair<>(fromUnit,toUnit);
    FactorOfConversion factor = focs.get(forPair);
    if(factor==null)
      return null;
    
    Pair<Unit,Unit> foundPair = focs.keySet().stream()
                                    .filter(pair -> pair.equals(forPair))
                                    .findFirst()
                                    .orElse(null);
    if(foundPair!=null)
      return foundPair.getKey()==forPair.getKey() ? factor : factor.inverse();  
    else
      return null;    
  }
  
  private static String normalize(String symbol) {
      return symbol.trim().toLowerCase();
  }

  public static boolean exists(String aSymbol) {
    String symbol = normalize(aSymbol);
    int count = (int) units.stream()
                           .filter(unit->unit.symbol()==symbol)
                           .count();
     return count==1;
  }
  
  public static void createFactorOfConversion(Unit fromUnit, Unit toUnit, FactorOfConversion factor) {
    if(fromUnit==null || toUnit==null || factor==null)
      throw new IllegalArgumentException();
    
    if(fromUnit.equals(toUnit)) {
      if(!factor.equals(FactorOfConversion.UNITY))
        throw new IllegalArgumentException();
      return;
    }
    
    focs.put(new Pair<Unit, Unit>(fromUnit,toUnit), factor);
  }  
 
}
