package com.googlecode.tawus;

import org.testng.annotations.Test;

import com.googlecode.tawus.annotations.InjectEntitySelectSupport;

public class InjectEntitySelectSupportTest extends BaseTestCase
{

   @Test
   public void exception_is_thrown_if_criteria_is_not_provided()
   {
      openBaseURL();
      clickAndWait("link=InjectEntitySelectSupport Demo With No Criteria");

      assertTextPresent("SearchCriteria cannot be null when annotated with @"
            + InjectEntitySelectSupport.class.getSimpleName());
   }

   @Test
   public void annotation_works_for_select_component()
   {
      openBaseURL();
      clickAndWait("link=InjectEntitySelectSupport Demo");
      
      assertTrue(isElementPresent("user"));
      assertTrue(isElementPresent("//label[@for='user']"));
      
      select("user", "label=Taha");
      clickAndWait("//input[@type='submit']");
      assertTextPresent("Taha");
   }
}
