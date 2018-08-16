package com.codechallenge.intent;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;

import com.codechallenge.intent.Expression.ExpressionBuilder;
import com.codechallenge.intent.termtype.TermType;

public class IntentDefinitionLoader {
  
  private enum State {
    NOT_LOADED,
    LOADING,
    LOADED
  }
  
  private static State loadState = State.NOT_LOADED;
  private static List<Intent> intents;
  private static List<Expression> expressions;
  
  private IntentDefinitionLoader() {}

  public static void load(Reader reader) {
    
    if(loadState == State.LOADING)
      throw new IllegalStateException();
    
    start();
    
    try {
      
      JsonReader jsonReader = Json.createReader(reader);
      JsonObject rootObject = jsonReader.read().asJsonObject();      
      
      JsonArray intentsJson = Optional.ofNullable(rootObject.getJsonArray("intents"))
                                      .orElseThrow(()-> new IllegalDefinitionFormat("property \"intents\""
                                          + " missing in the root object"));
                  
      intentsJson.forEach(intent->{
        if(intent.getValueType() != JsonValue.ValueType.OBJECT)
          throw new IllegalDefinitionFormat();
        parseIntent(intent.asJsonObject());
      });
      
      Intents.loadIntents(intents);
      Expressions.loadExpressions(expressions);
      
      complete();
      
    }catch (Exception e) {
      abort();
      throw new IllegalDefinitionFormat(e);
    }
    
  }

  private static void start() {    
    loadState = State.LOADING;
    intents = new ArrayList<>();
    expressions = new ArrayList<>();
  }

  private static void parseIntent(JsonObject intentJson) {

    String name;
    try {
      name = Optional.ofNullable(intentJson.getJsonString("name"))
                     .map(JsonString::getString)
                     .orElseThrow(() -> new IllegalDefinitionFormat("Mandatory property "
                         + "\"name\" missing in the definition of intent"));

    }catch(ClassCastException e) {
      throw new IllegalDefinitionFormat("String value expected for property \"name\"");
    }

    Intent.Nature nature;
    try {
      nature = Optional.ofNullable(intentJson.getJsonString("nature"))
                       .map(JsonString::getString)
                       .map(Intent.Nature::valueOf)
                       .orElseThrow(() -> new IllegalDefinitionFormat("Mandatory property "
                           + "\"nature\" missing in the definition of intent"));

    }catch(ClassCastException e) {
      throw new IllegalDefinitionFormat("String value expected for property \"nature\"");
    }catch(IllegalArgumentException e) {
      throw new IllegalDefinitionFormat("Unexpected value for property \"nature\"");
    }    

    JsonArray array;
    List<Term> terms = null;
    if(intentJson.containsKey("terms")) {

      try {
        array = intentJson.getJsonArray("terms");
      }catch(ClassCastException e) {
        throw new IllegalDefinitionFormat("\"terms\" is expected to be an array"); 
      }

      terms = parseTerms(array);
    }
    
    
    try {
        array = Optional.ofNullable(intentJson.getJsonArray("expressions"))
                        .orElseThrow(()-> new IllegalDefinitionFormat("Mandatory property "
                            + "\"expressions\" of type ARRAY missing for intent"));
    }catch(ClassCastException e) {
      throw new IllegalDefinitionFormat("\"expressions\" is expected to be an array"); 
    }
    
    if(array.isEmpty())
      throw new IllegalDefinitionFormat("At least one expression for intent is expected");
    
        
    Intent intent = Intent.newIntent()
                          .withName(name)
                          .ofNature(nature)
                          .havingTerms(terms)
                          .create();
    intents.add(intent);    
    expressions.addAll(parseExpressions(intent,array));

  }           
    
  
  private static void abort() {
     intents = null;
     expressions = null;
     loadState = State.NOT_LOADED;
  }
  
  private static void complete() {
    intents = null;
    expressions = null;
    loadState = State.LOADED;
  }  

  private static List<Term> parseTerms(JsonArray termsJson) {
     List<Term> terms = new ArrayList<>();
     
     termsJson.forEach(value -> {
       JsonObject termJson;
       termJson = Optional.ofNullable(value.asJsonObject())
                          .orElseThrow(()-> new IllegalDefinitionFormat(
                              "Definition of terms expected"));
       
       String name;
       try {
         name = Optional.ofNullable(termJson.getJsonString("name"))
                        .map(JsonString::getString)
                        .orElseThrow(()-> new IllegalDefinitionFormat(
                            "Mandatory property \"name\" of term missing"));
         
       }catch(ClassCastException e) {
         throw new IllegalDefinitionFormat("String value expected for "
             + "property \"name\" of term");
       }
       
       TermType.Name typeName;
       try {
         typeName = Optional.ofNullable(termJson.getJsonString("type"))
                        .map(JsonString::getString)
                        .map(TermType.Name::valueOf)
                        .orElseThrow(()-> new IllegalDefinitionFormat(
                            "Mandatory property \"name\" of term missing"));
         
       }catch(ClassCastException e) {
         throw new IllegalDefinitionFormat("String value expected for "
             + "property \"name\" of term");
       }catch(IllegalArgumentException e) {
         throw new IllegalDefinitionFormat("Unexpected value for property"
             + " \"type\" of term");
       }
       
       terms.add(new Term(name,typeName));       
       
     });
     
     return terms;
  }  

  private static List<Expression> parseExpressions(Intent intent,JsonArray expressionsJson) {
    List<Expression> expressions = new ArrayList<>();
    
    expressionsJson.forEach(value -> {

      if(value.getValueType()!=JsonValue.ValueType.STRING)
        throw new IllegalDefinitionFormat();
      
      String expression = value.toString();
      expression = expression.substring(1, expression.length()-1);
      expressions.add(parseSingleExpression(intent,expression));
    });
    
    return expressions;    
  }

  private static Expression parseSingleExpression(Intent intent,String expression) {

    Matcher matcher = Pattern.compile("\\{([^\\}]*)\\}",Pattern.CASE_INSENSITIVE)
                             .matcher(expression);

    ExpressionBuilder builder = Expression.newOne();
    builder.ofIntent(intent);

    Term term;
    String phrase,termName;
    int partsCount = 0;

    matcher.region(0,expression.length());
    while(matcher.find()) {

      termName = expression.substring(matcher.start(1),matcher.end(1));
      phrase   = expression.substring(matcher.regionStart(),matcher.start());
      phrase   = phrase.trim();
      matcher.region(matcher.end(),matcher.regionEnd());

      if(!phrase.isEmpty()) {
        ++partsCount;
        if(partsCount==1)
          builder.beginsWithPhrase(phrase);
        else
          builder.followedByPhrase(phrase);
      }

      if(!termName.isEmpty()) {
        term = Optional.ofNullable(intent.getTerm(termName))
                       .orElseThrow(()-> new IllegalDefinitionFormat("Term with this name "
                           + "is not defined in the intent"));
        ++partsCount;
        if(partsCount==1)
          builder.beginsWithTerm(term);
        else
          builder.followedByTerm(term);

      }else {
        throw new IllegalDefinitionFormat("Expression has blank terms");
      }
    }

    phrase = expression.substring(matcher.regionStart(), matcher.regionEnd());
    if(!phrase.isEmpty()){
      ++partsCount;
      if(partsCount==1)
        builder.beginsWithPhrase(phrase);
      else
        builder.followedByPhrase(phrase);      
    }

    return builder.create();

  }
}
