package com.codechallenge.units;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;

class UnitTests {

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    String patternRegex = "(?<num1>(?:\\d+\\.)?\\d*)(?<unit1>\\w+)=(?<num2>(?:\\d+\\.)?\\d*)(?<unit2>\\w+)";
    String[] units = {"mm","cm","m","km","in","ft"}; 
    String[] focs  = {"cm=10mm","km=1000m","ft=12in","in=25.4mm"};
    
    Arrays.asList(units).forEach(symbol->Unit.create(symbol));
    Arrays.asList(focs).forEach(s -> {
      Matcher matcher = Pattern.compile(patternRegex).matcher(s);
      if(!matcher.matches())
        throw new IllegalStateException();      
      
      float num1 = Optional.ofNullable(matcher.group("num1"))
                           .map(Float::parseFloat)
                           .orElse(1f);
    
      Unit fromUnit = Optional.ofNullable(matcher.group("unit1"))
                              .map(Unit::get)
                              .orElseThrow(()-> new IllegalStateException());
      
      float num2 = Optional.ofNullable(matcher.group("num2"))
                           .map(Float::parseFloat)
                           .orElse(1f);
      
      Unit toUnit = Optional.ofNullable(matcher.group("unit2"))
                            .map(Unit::get)
                            .orElseThrow(()-> new IllegalStateException());
      
      FactorOfConversion factor = new FactorOfConversion(num1,num2);
      Unit.createFactorOfConversion(fromUnit, toUnit, factor);
    });    
  }

  @ParameterizedTest
  @CsvSource({"30,m,km,0.03"})
  public void ShouldConvertUnitsWhenConversionExists(float qty, @ConvertWith(ToUnitConverter.class) Unit ofUnit, 
          @ConvertWith(ToUnitConverter.class) Unit toUnit, float expectedResult) {
    
    FactorOfConversion factor = Unit.getFactorOfConversion(ofUnit, toUnit);
    float convertedQty = qty * factor.numerator() / factor.denominator();
    assertThat(convertedQty,is(expectedResult));
    
  }
  
  public class ToUnitConverter extends SimpleArgumentConverter {

    @Override
    protected Object convert(Object source, Class<?> targetType)
        throws ArgumentConversionException {
        String symbol = String.valueOf(source);
        return Unit.create(symbol);        
    }
    
  }

}
