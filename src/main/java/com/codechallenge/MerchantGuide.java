package com.codechallenge;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.codechallenge.intent.IntentDefinitionLoader;
import com.codechallenge.intent.IntentRequest;
import com.codechallenge.intent.IntentResolver;
import com.codechallenge.intent.Intents;
import com.codechallenge.intent.Sentence;
import com.codechallenge.intent.api.IntentHandler;
import com.codechallenge.intent.termtype.TermType;

public class MerchantGuide {
  
  private static final String INTENTS_LOCATION = "/intent-definitions/intents.json";
  
  static {
    bootstrap();
  }  

  public static void main(String[] args) {    
    
    List<Sentence> sentences = new ArrayList<>();
    
    if(args.length==0) {
      System.out.print("Input a completely qualified name of the File");
      return;
    }
    
    String filename = args[0];
    try(BufferedReader reader = new BufferedReader(new FileReader(filename))
       ){

      String line = reader.readLine();
      while(line!=null) {        
        sentences.add(new Sentence(line));
        line = reader.readLine();
      }
    
    } catch (IOException e) {
      System.out.print(e.getMessage());      
    }
    
    for(Sentence sentence : sentences) {
      IntentRequest request = new IntentRequest();
      request.setSentence(sentence);
      
      System.out.println(sentence.actualText());
      respond(request);
      System.out.println(request.response().toString());
      
      System.out.println();
      
    }    
    
  }
  
  public static void respond(IntentRequest request) {
    
    String resolvedIntent;
    IntentResolver resolver = IntentResolver.ofSentence(request.sentence());
    resolver.resolve();
    
    resolvedIntent = Optional.ofNullable(resolver.getResolvedIntent()).orElse(null);
    if(resolvedIntent==null || resolvedIntent.isEmpty()) {
       request.response().inform("I have no idea what you are talking about");
       return;
    }    
    
    request.setNameOfIntent(resolvedIntent);
    request.setResolvedTerms(resolver.getResolvedTerms());
    
    IntentHandler handler = Intents.findByName(resolvedIntent).handler();
    if(handler==null) {
      request.response().inform("Sorry! I don't know how to do it");
      return;
    }
    
    handler.processRequest(request);
    
  }
  
  private static void bootstrap() {
    
    InputStream inputStream = MerchantGuide.class.getResourceAsStream(INTENTS_LOCATION);
    Reader reader = new InputStreamReader(inputStream);
    
    IntentDefinitionLoader.load(reader);
    TermType.scanMatchers();
    
  }  

}
