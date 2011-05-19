package com.googlecode.tawus.components;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

public class EntityGridWithEditAjaxTest extends SeleniumTestCase {
   
   @Test
   public void test_edit_link() throws InterruptedException {
      openBaseURL();
      clickAndWait("link=Entity Grid with Edit");
      
      assertTexts("xpath=//table[@class='t-data-grid'][1]/thead[1]/tr[1]/th[%d]/a[1]",
            new String[] { "Name", "Id", "Address", "Age", "Gender" });

      assertTexts("xpath=//table[@class='t-data-grid'][1]/tbody[1]/tr[1]/td[%d]", new String[] {
            "Taha", "1", "Srinagar", "32", "Male", "Computers" });
      
      click("xpath=//table[@class='t-data-grid'][1]/tbody[1]/tr[1]/td[1]/a[1]");//edit link
      Thread.sleep(1500);
      
      assertTextPresent("User Details");
      
      //Fill in the contents
      Map<String, String> params = new HashMap<String, String>();
      params.put("name", "TawusHafeez");
      params.put("address", "Srinagar, Kashmir");
      params.put("age", "44");
      params.put("department", "2");//Select option value for Computers
      params.put("id_0", "11");
      params.put("gender", "2");//Female
      
      //Check if all the fields are present
      for (String field : params.keySet()) {
         assertTrue(isElementPresent("name=" + field), "Field " + field + " is present");
      }
      
      
      type("name=name", params.get("name"));
      type("name=address", params.get("address"));
      type("name=age", params.get("age"));
      type("name=id_0", params.get("id_0"));
      select("name=department", "value=2");
      select("name=gender", "value=Female");

      click("//form[1]//input[@value='Save'][1]");
      Thread.sleep(1500);
      assertText("id=message", params.get("name") + "/" + params.get("address"));
      
      assertTexts("xpath=//table[@class='t-data-grid'][1]/thead[1]/tr[1]/th[%d]/a[1]",
            new String[] { "Name", "Id", "Address", "Age", "Gender" });

      assertTexts("xpath=//table[@class='t-data-grid'][1]/tbody[1]/tr[1]/td[%d]", new String[] {
            "TawusHafeez", "11", "Srinagar, Kashmir", "44", "Female", "English" });
      
      click("xpath=//table[@class='t-data-grid'][1]/tbody[1]/tr[1]/td[1]/a[1]");//edit link
      Thread.sleep(1000);
      params.clear();
      params.put("name", "Taha");
      params.put("address", "Srinagar");
      params.put("age", "32");
      params.put("department", "2");
      params.put("id_0", "1");
      params.put("gender", "1");
      
      type("name=name", params.get("name"));
      type("name=address", params.get("address"));
      type("name=age", params.get("age"));
      type("name=id_0", params.get("id_0"));
      select("name=department", "value=2");
      select("name=gender", "value=Female");

      click("//form[1]//input[@value='Save'][1]");
      Thread.sleep(1500);
      assertText("id=message", params.get("name") + "/" + params.get("address"));
      
      click("xpath=//table[@class='t-data-grid'][1]/tbody[1]/tr[1]/td[1]/a[1]");//edit link
      Thread.sleep(1500);
      click("//form[1]//button[2]");//cancel button
      Thread.sleep(1500);
      assertText("id=message", "canceled");
      
      click("xpath=//table[@class='t-data-grid'][1]/tbody[1]/tr[1]/td[1]/a[1]");//edit link
      Thread.sleep(1500);
      click("//form[1]//button[1]");//delete button
      Thread.sleep(1500);
      assertText("id=message", "deleted");
   }


   //@Test
   public void test_new_link() throws InterruptedException {
      openBaseURL();
      clickAndWait("link=Entity Grid with Edit");
      
      assertTexts("xpath=//table[@class='t-data-grid'][1]/thead[1]/tr[1]/th[%d]/a[1]",
            new String[] { "Name", "Id", "Address", "Age", "Gender" });

      assertTexts("xpath=//table[@class='t-data-grid'][1]/tbody[1]/tr[1]/td[%d]", new String[] {
            "Taha", "1", "Srinagar", "32", "Male", "Computers" });
      
      click("xpath=//div[@class='t-entity-new'][1]/a[1]");
      Thread.sleep(1500);
      
      assertTextPresent("User Details");
      
      //Fill in the contents
      Map<String, String> params = new HashMap<String, String>();
      params.put("name", "TawusHafeez");
      params.put("address", "Srinagar, Kashmir");
      params.put("age", "44");
      params.put("department", "2");//Select option value for Computers
      params.put("id_0", "11");
      params.put("gender", "2");//Female
      
      //Check if all the fields are present
      for (String field : params.keySet()) {
         assertTrue(isElementPresent("name=" + field), "Field " + field + " is present");
      }
      
      type("name=name", params.get("name"));
      type("name=address", params.get("address"));
      type("name=age", params.get("age"));
      type("name=id_0", params.get("id_0"));
      select("name=department", "value=2");
      select("name=gender", "value=Female");

      click("//form[1]//input[@value='Save'][1]");
      Thread.sleep(1500);
      assertText("id=message", params.get("name") + "/" + params.get("address"));
      
      assertTexts("xpath=//table[@class='t-data-grid'][1]/thead[1]/tr[1]/th[%d]/a[1]",
            new String[] { "Name", "Id", "Address", "Age", "Gender" });

      assertTexts("xpath=//table[@class='t-data-grid'][1]/tbody[1]/tr[1]/td[%d]", new String[] {
            "Taha", "1", "Srinagar", "32", "Male", "Computers" });
      
      click("xpath=//div[@class='t-entity-new'][1]/a[1]");
      Thread.sleep(1500);
      
      assertTextPresent("User Details");
      click("//form[1]//button[2]");//cancel button
      Thread.sleep(1500);
      assertText("id=message", "canceled");
      
      click("xpath=//div[@class='t-entity-new'][1]/a[1]");
      Thread.sleep(1500);
      click("//form[1]//button[1]");//delete button
      Thread.sleep(1500);
      assertText("id=message", "deleted");
   }


   private void assertTexts(String xpath, String[] values) {
      for (int i = 0; i < values.length; ++i) {
         assertText(String.format(xpath, i + 1), values[i]);
      }
   }
   

}
