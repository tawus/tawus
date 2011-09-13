package com.googlecode.tawus;

import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

public class EntityGridDemoTest extends SeleniumTestCase {

   @Test
   public void test_criteria_disabled_page() throws InterruptedException {
      openBaseURL();
      clickAndWait("link=Entity Grid with Criteria Disabled");
      this.assertTextPresent("There is no data to display");
   }

   @Test
   public void test_criteria_enabled_page() throws InterruptedException {
      openBaseURL();
      clickAndWait("link=Entity Grid with Criteria Enabled");
      assertTexts("xpath=//table[@class='t-data-grid'][1]/thead[1]/tr[1]/th[%d]/a[1]",
            new String[] { "Name", "Id", "Address", "Age", "Gender" });

      assertTexts("xpath=//table[@class='t-data-grid'][1]/tbody[1]/tr[1]/td[%d]", new String[] {
            "Taha", "1", "Srinagar", "32", "Male", "Computers" });
      
      //Try sorting. It is not going to work as the dao does not support it, but we can atleast
      //check if criteria has changed 
      clickAndCheckMessage("xpath=//table[@class='t-data-grid'][1]/thead[1]/tr[1]/th[%d]/a[1]",
            new String[] { "Name", "Id", "Address", "Age", "Gender" }, false);
   }
   
   private void clickAndCheckMessage(String xpath, String[] links, boolean ajax) throws InterruptedException {
      for (int i = 0; i < links.length; ++i) {
         click(String.format(xpath, i + 1));
         if(ajax){
            Thread.sleep(1000);
         }else {
            waitForPageToLoad();
         }
         
         assertText("xpath=//div[@class='sortColumn'][1]", links[i].toLowerCase() + "/true");
         click(String.format(xpath, i + 1));
         if(ajax){
            Thread.sleep(1000);
         }else {
            waitForPageToLoad();
         }
         
         assertText("xpath=//div[@class='sortColumn'][1]", links[i].toLowerCase() + "/false");
      }      
   }
   

   private void assertTexts(String xpath, String[] values) {
      for (int i = 0; i < values.length; ++i) {
         assertText(String.format(xpath, i + 1), values[i]);
      }
   }
   

   @Test
   public void test_criteria_disabled_page_with_ajax() throws InterruptedException {
      openBaseURL();
      click("link=Entity Grid Ajax with Criteria Disabled");
      Thread.sleep(2000);
      assertTextPresent("There is no data to display");
   }

   @Test
   public void test_criteria_enabled_page_with_ajax() throws InterruptedException {
      openBaseURL();
      click("link=Entity Grid Ajax with Criteria Enabled");
      Thread.sleep(2000);
      assertTexts("xpath=//table[@class='t-data-grid'][1]/thead[1]/tr[1]/th[%d]/a[1]",
            new String[] { "Name", "Id", "Address", "Age", "Gender" });

      assertTexts("xpath=//table[@class='t-data-grid'][1]/tbody[1]/tr[1]/td[%d]", new String[] {
            "Taha", "1", "Srinagar", "32", "Male", "Computers" });
      
      //Try sorting. It is not going to work as the dao does not support it, but we can atleast
      //check if criteria has changed 
      clickAndCheckMessage("xpath=//table[@class='t-data-grid'][1]/thead[1]/tr[1]/th[%d]/a[1]",
            new String[] { "Name", "Id", "Address", "Age", "Gender" }, true);
   }

}
