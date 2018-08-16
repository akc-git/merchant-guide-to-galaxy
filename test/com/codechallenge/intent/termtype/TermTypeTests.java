package com.codechallenge.intent.termtype;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
class TermTypeTests {

  @Test
  void ShouldEnsureAllMatchersImplemented(){
    boolean allMatchersImplemented = true;
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
