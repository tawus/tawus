package com.googlecode.tawus;

import org.testng.annotations.Test;

public class EntityGridWithSearchDemoTest extends BaseTestCase {

   @Test
   public void test_search_ajax() throws InterruptedException {
      openBaseURL();
      clickAndWait("link=Entity Grid Ajax with Search");
      this.assertTextPresent("There is no data to display");
      
      
      click("id=search");
      Thread.sleep(1000);
      
      assertTexts("xpath=//table[@class='t-data-grid'][1]/thead[1]/tr[1]/th[%d]/a[1]",
            new String[] { "Name", "Id", "Address", "Age", "Gender" });

      assertTexts("xpath=//table[@class='t-data-grid'][1]/tbody[1]/tr[1]/td[%d]", new String[] {
            "Taha", "1", "Srinagar", "32", "Male", "Computers" });
            
      type("xpath=//form[1]//input[@name='name'][1]", "Taha");
      type("xpath=//form[1]//input[@name='address'][1]", "Srinagar");
      
      click("xpath=//form[1]//input[@name='search'][1]");
      assertTextUsingJS("searchFields", "Taha/Srinagar");
      
      click("xpath=//form[1]//input[@value='Cancel'][1]");
      assertTextUsingJS("searchFields", "none");
   }

   @Test
   public void test_search() throws InterruptedException {
      openBaseURL();
      clickAndWait("link=Entity Grid with Search");
      this.assertTextPresent("There is no data to display");
      
      
      clickAndWait("id=search");
      
      assertTexts("xpath=//table[@class='t-data-grid'][1]/thead[1]/tr[1]/th[%d]/a[1]",
            new String[] { "Name", "Id", "Address", "Age", "Gender" });

      assertTexts("xpath=//table[@class='t-data-grid'][1]/tbody[1]/tr[1]/td[%d]", new String[] {
            "Taha", "1", "Srinagar", "32", "Male", "Computers" });
            
      type("xpath=//form[1]//input[@name='name'][1]", "Taha");
      type("xpath=//form[1]//input[@name='address'][1]", "Srinagar");
      
      clickAndWait("xpath=//form[1]//input[@name='search'][1]");
      assertText("searchFields", "Taha/Srinagar");
      
      clickAndWait("xpath=//form[1]//input[@value='Cancel'][1]");
      assertText("searchFields", "none");
   }

   private void assertTexts(String xpath, String[] values) {
      for (int i = 0; i < values.length; ++i) {
         assertText(String.format(xpath, i + 1), values[i]);
      }
   }
   

}
