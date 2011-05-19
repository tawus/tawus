package com.googlecode.tawus.components;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

public class EntityEditFormAjaxTest extends SeleniumTestCase {
   
   @Test
   public void test_entity_save() throws InterruptedException {
      openBaseURL();
      clickAndWait("link=Entity Edit Form Ajax");
      
      Map<String, String> params = new HashMap<String, String>();
      params.put("name", "Tawus");
      params.put("address", "Srinagar");
      params.put("age", "32");
      params.put("department", "1");//Select option value for Computers
      params.put("id_0", "10");
      params.put("gender", "1");//Male
      params.put("otherDepartments-values", "[1]");
      
      //Check if all the fields are present
      for (String field : params.keySet()) {
         assertTrue(isElementPresent("name=" + field), "Field " + field + " is present");
      }
      
      type("name=name", params.get("name"));
      type("name=address", params.get("address"));
      type("name=age", params.get("age"));
      type("name=id_0", params.get("id_0"));
      select("name=department", "value=1");
      select("name=gender", "value=Male");

      click("id=save");
      Thread.sleep(1000);
      this.assertText("id=message", "Tawus created");
   }

   @Test
   public void test_entity_cancel() throws InterruptedException {
      openBaseURL();
      clickAndWait("link=Entity Edit Form Ajax");
      click("id=cancel");
      Thread.sleep(1000);
      this.assertText("id=message", "Message cleared");
   }
}
