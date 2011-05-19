package com.googlecode.tawus

import org.apache.tapestry5.test.PageTester

import spock.lang.Shared
import spock.lang.Specification

import com.googlecode.tawus.app0.services.AppModule;
import com.googlecode.tawus.services.TawusModule

class TestSpecification extends Specification {
   @Shared PageTester pageTester

   def setupSpec(){
      pageTester = new PageTesterWithEntityDef("com.googlecode.tawus.app0", 
         "app", "src/test/webapp", TawusModule.class, AppModule.class);
   }
}
