package com.googlecode.tawus;

import org.apache.tapestry5.test.PageTester;
import org.apache.tapestry5.test.TapestryTestCase;
import org.testng.annotations.BeforeMethod;

import com.googlecode.tawus.app0.services.AppModule;
import com.googlecode.tawus.services.TawusModule;

public class BaseUnitTestCase extends TapestryTestCase 
{
   protected PageTester pageTester;

   @BeforeMethod
   public void setupPageTester(){
      pageTester = new PageTesterWithEntityDef("com.googlecode.tawus.app0", 
         "app", "src/test/webapp", TawusModule.class, AppModule.class);
   }
   
}
