package com.googlecode.tawus;

import org.testng.annotations.Test;

public class InjectDAOTest extends BaseTestCase
{
   
   @Test
   public void injectdao_works()
   {
      openBaseURL();
      clickAndWait("link=InjectDAO Demo");
      
      assertTextPresent("Total Users : 1");
   }
   
   @Test
   public void injectdao_fails_for_class_whose_service_has_not_been_configured()
   {
      openBaseURL();
      clickAndWait("link=InjectDAO Not Present Demo");
      
      assertTextPresent("No service configured for entity class java.lang.String");
   }

}
