package com.googlecode.tawus.components;

import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

public class EntityGridWithSearchAjaxTest extends SeleniumTestCase {

   @Test
   public void test_search() throws InterruptedException {
      openBaseURL();
      clickAndWait("link=Entity Grid with Search");
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
      Thread.sleep(1000);
      assertText("id=searchFields", "Taha/Srinagar");
      
      click("xpath=//form[1]//input[@name='cancel'][1]");
      Thread.sleep(1000);
      assertText("id=searchFields", "none");
   }


   private void assertTexts(String xpath, String[] values) {
      for (int i = 0; i < values.length; ++i) {
         assertText(String.format(xpath, i + 1), values[i]);
      }
   }
   

}
