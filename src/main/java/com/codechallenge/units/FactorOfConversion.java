package com.codechallenge.units;

public class FactorOfConversion {
  
  private float numerator;
  private float denominator;
  
  public static FactorOfConversion UNITY = new FactorOfConversion(1,1);
  
  public FactorOfConversion(int numerator, int denominator){
    if(numerator==0 || denominator==0) 
      throw new IllegalArgumentException();
    this.numerator = numerator;
    this.denominator = denominator;
  }
  
  public FactorOfConversion(float numerator,float denominator) {
    if(numerator==0 || denominator==0) 
      throw new IllegalArgumentException();
    this.numerator = numerator;
    this.denominator = denominator;
  }
  
  public float numerator() {
    return this.numerator;
  }
  
  public float denominator() {
    return this.denominator;
  }
  
  public FactorOfConversion inverse() {
    return new FactorOfConversion(this.denominator, this.numerator);
  }
  
  public FactorOfConversion multiply(FactorOfConversion foc) {
    return new FactorOfConversion(this.denominator * foc.denominator(),
                                  this.numerator() * foc.numerator());
  }

}
