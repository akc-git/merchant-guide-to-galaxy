package com.codechallenge.intent;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
  @Parameters({"correct.json,1"})
  public void ShouldLoadIntentsSuccessfullyWhenFormatIsCorrect(String filename, int actualNumberOfIntents) {
    
    
    Reader reader = new InputStreamReader(
                  this.getClass().getResourceAsStream("/intent-definitions/" + filename));
    
    IntentDefinitionLoader.load(reader);
    assertEquals(Intents.findAll().size(), actualNumberOfIntents);
  }

  @Test
  @Parameters({"intentsMissing.json",
               "intentsNotAnArray.json",
               "nameOfIntentMissing.json",
               "nameOfIntentIsNotString.json",
               "natureOfIntentMissing.json",
               "natureOfIntentIsInvalid.json",
               "termsIsNotAnArray.json",
               "expressionsMissing.json",
               "expressionsIsNotAnArray.json",
               "expressionsIsAnEmptyArray.json",
               "expressionContainsUndefinedTerms.json"})
  public void ShouldThrowExceptionWhenFormatIsIncorrect(String filename) throws Exception {
    
    
    Reader reader = new InputStreamReader(
                  this.getClass().getResourceAsStream("/intent-definitions/" + filename));    
    
    assertThrows(IllegalDefinitionFormat.class, ()-> {
      IntentDefinitionLoader.load(reader);
    });
  }
}
