package com.codechallenge.units;

public class FactorOfConversion {
  
  private int numerator;
  private int denominator;
  
  public static FactorOfConversion UNITY = new FactorOfConversion(1,1);
  
  public FactorOfConversion(int numerator, int denominator){
    if(numerator==0 || denominator==0) 
      throw new IllegalArgumentException();
    this.numerator = numerator;
    this.denominator = denominator;
  }
  
  public int numerator() {
    return this.numerator;
  }
  
  public int denominator() {
    return this.denominator;
  }
  
  public FactorOfConversion inverse() {
    return new FactorOfConversion(this.denominator, this.numerator);
  }

}
