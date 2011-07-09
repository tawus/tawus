package com.googlecode.tawus.addons.components;

import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.test.TapestryTestCase;
import org.testng.annotations.Test;

public class TabPanelTest extends TapestryTestCase
{
   
   @Test(expectedExceptions = IllegalArgumentException.class)
   public void test_if_setup_render_throws_exception_if_no_tabs_are_specified()
   {      
     TabPanel component = new TabPanel(null, null, null, null, null, null);
     
     component.setupRender();
   }
   
   @Test
   public void test_if_setup_render_sets_active_to_first_value_if_not_specified()
   {
      JavaScriptSupport javaScriptSupport = mockJavaScriptSupport();
      
      TabPanel component = new TabPanel("testTab", null, null,  javaScriptSupport, null, null);
      
      expect(javaScriptSupport.allocateClientId((String)null)).andReturn("testId");
      
      replay();
      
      component.setupRender();
      assertEquals(component.getActive(), "testTab");
      
      verify();
   }
   
   @Test
   public void test_setup_render_does_not_set_active_to_first_value_if_specified()
   {
      JavaScriptSupport javaScriptSupport = mockJavaScriptSupport();
      
      TabPanel component = new TabPanel("testTab", "activeTab", null,  javaScriptSupport, null, null);
      
      expect(javaScriptSupport.allocateClientId((String)null)).andReturn("testId");
      
      replay();
      
      component.setupRender();
      assertEquals(component.getActive(), "activeTab");
      
      verify();
   }
   
   

}
