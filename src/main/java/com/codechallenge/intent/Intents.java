package com.codechallenge.intent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;

import com.codechallenge.intent.api.IntentHandler;
import com.codechallenge.intent.termtype.TermMatcher;

public class Intents {
  
  private static List<Intent> intents = new ArrayList<>();
  private static Map<String,Class<?>> handlers;
  
  private Intents() {}
  
  public static Intent findByName(String name) {
    return intents.stream()
                  .filter(intent->intent.name().equals(name))
                  .findFirst()
                  .orElse(null);
  }
  
  public static List<Intent> findByNature(Intent.Nature nature){
    return intents.stream()
        .filter(intent->intent.nature().equals(nature))
        .collect(Collectors.toList());
  }
  
  public static List<Intent> findAll() {
    return intents;
  }
  
  public static void loadIntents(List<Intent> intentsToLoad) {
    intents = intentsToLoad;
    scanHandlers();
  }

  @SuppressWarnings("unchecked")
  private static void scanHandlers() {
    
    handlers = new HashMap<>();
    Reflections reflections = new Reflections("com.codechallenge");
    Set<Class<?>> scannedMatchers = reflections.getTypesAnnotatedWith(Handler.class);
    
    for(Class<?> claz : scannedMatchers) {
      if(!claz.isAnnotationPresent(Handler.class) || 
         !IntentHandler.class.isAssignableFrom(claz))
        continue;
      
      Handler annotation = claz.getAnnotation(Handler.class);
      String nameOfIntent = annotation.forIntent();
      handlers.putIfAbsent(nameOfIntent,claz);
    }
  }

  public static Class<?> getTypeOfHandler(String nameOfIntent) {
    return handlers.get(nameOfIntent);
  }
  
}
