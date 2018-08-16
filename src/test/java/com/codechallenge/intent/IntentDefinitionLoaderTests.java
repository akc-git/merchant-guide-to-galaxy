package com.codechallenge.intent;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class IntentDefinitionLoaderTests {

  @Test
  @Parameters({"correct.json"})
  public void ShouldLoadIntentsSuccessfullyWhenFormatIsCorrect(String filename) {
    
    
    Reader reader = new InputStreamReader(
                  this.getClass().getResourceAsStream("/intent-definitions/" + filename));
    
    IntentDefinitionLoader.load(reader);    
  }

  @Test
  @Parameters({"nameOfIntentMissing.json"})
  public void ShouldThrowExceptionWhenFormatIsIncorrect(String filename) throws Exception {
    
    
    Reader reader = new InputStreamReader(
                  this.getClass().getResourceAsStream("/intent-definitions/" + filename));    
    
    assertThrows(IllegalDefinitionFormat.class, ()-> {
      IntentDefinitionLoader.load(reader);
    });
  }
}
