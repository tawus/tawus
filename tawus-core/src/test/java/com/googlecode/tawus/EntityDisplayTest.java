package com.googlecode.tawus;

import org.testng.annotations.Test;

public class EntityDisplayTest extends BaseTestCase
{

   @Test
   public void properties_are_properly_displayed_in_table() throws Exception
   {
      openBaseURL();
      clickAndWait("link=EntityDisplay Demo");
      
      assertText("//tr[1]/td[1]", "Name");
      assertText("//tr[1]/td[2]", "Taha Hafeez");
      
      assertText("//tr[1]/td[3]", "Dob");
      
      assertText("//tr[2]/td[1]", "Address");
   }
   
}
