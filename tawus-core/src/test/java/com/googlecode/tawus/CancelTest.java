package com.googlecode.tawus;

import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

public class CancelTest extends SeleniumTestCase
{
   @Test
   public void test_cancel_button_cancels_a_form()
   {
      openBaseURL();
      clickAndWait("link=Cancel Demo");
      
      clickAndWait("cancelForm");
      assertText("message", "Form Cancelled");
   }
   
   @Test
   public void test_cancel_button_cancels_ajax_form_using_ajax()
   {
      openBaseURL();
      clickAndWait("link=Cancel Demo");
      
      click("cancelAjaxForm");
      assertTextUsingJS("message", "Ajax Form Cancelled");
   }

   private void assertTextUsingJS(String id, String text)
   {
      waitForCondition(String.format(
            "selenium.browserbot.getCurrentWindow().$('%s').innerHTML=='%s'", id, text),
            PAGE_LOAD_TIMEOUT);
   }
}
