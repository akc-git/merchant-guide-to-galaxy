package com.codechallenge.intent.termtype;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;

class TermTypeTests {

  @Test
  void ShouldEnsureAllMatchersImplemented(){
    boolean allMatchersImplemented = true;
    TermType.scanMatchers();
    
    for(TermType.Name name : TermType.Name.values() ) {
      try {
        if(TermType.getMatcher(name)==null) {
          allMatchersImplemented = false;
          break;
        }
      } catch (InstantiationException | IllegalAccessException | IllegalStateException e) {
        allMatchersImplemented = false;
      }
    }
    assertThat(allMatchersImplemented, is(true));
  }

}
