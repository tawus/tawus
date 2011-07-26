package com.googlecode.tawus.addons.integration;

import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

public class PreventDoubleSubmissionTest extends SeleniumTestCase
{
   @Test
   public void test_double_submit_for_ordinary_submission() throws Exception 
   {
      openBaseURL();
      clickAndWait("link=Prevent Form Submission Demo");
      clickAndWait("link=reset");
      
      //TODO: Simulate the test
   }
   
   @Test
   public void test_double_submit_when_mixin_is_added() throws Exception
   {
      openBaseURL();
      clickAndWait("link=Prevent Form Submission Demo");
      clickAndWait("link=reset");
      
      //TODO: Simulate the test
   }
}
