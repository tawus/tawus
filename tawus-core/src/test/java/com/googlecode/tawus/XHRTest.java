package com.googlecode.tawus;

import org.testng.annotations.Test;

public class XHRTest extends BaseTestCase
{
   @Test
   public void xhr_annotation_works_for_ajax()
   {
      openBaseURL();
      clickAndWait("link=XHR Demo With Ajax");
      
      click("link=Ajax Link");
      assertTextUsingJS("zone", "Clicked");
   }
   
   @Test
   public void xhr_annotation_works_without_ajax()
   {
      openBaseURL();
      clickAndWait("link=XHR Demo Without Ajax");
      
      assertTextUsingJS("zone", "Clicked");
   }
   
   @Test
   public void xhr_does_not_work_with_void_methods()
   {
      openBaseURL();
      clickAndWait("link=XHR Void Exception");
      
      assertTextPresent("XHR can be applied to non-void event handlers only");
   }

}
