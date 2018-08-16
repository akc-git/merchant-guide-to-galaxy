package com.codechallenge.intent;

import java.util.List;
import java.util.stream.Collectors;

public class Expressions {
  
  private static List<Expression> expressions;
  
  private Expressions() {}
  
  public static void loadExpressions(List<Expression> expressionsToLoad) {
    expressions = expressionsToLoad;
  }
  
  public static List<Expression> findByIntent(Intent intent) {
    return expressions.stream()
                      .filter(e->e.intent().equals(intent))
                      .collect(Collectors.toList());
  }
  
  public static List<Expression> findByNatureOfIntent(Intent.Nature nature) {
    return expressions.stream()
        .filter(e->e.intent().nature().equals(nature))
        .collect(Collectors.toList());
  }
  
  public static List<Expression> all(){
    return expressions;
  }

}
