package com.googlecode.tawus.addons.integration;

import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

public class TabPanelTest extends SeleniumTestCase
{
   @Test
   public void check_if_tab_panel_changes_content_on_clicking_title_links()
   {
      openBaseURL();
      clickAndWait("link=TabPanel Demo");
      
      //By default first tab should be displayed
      assertTextPresent("Content of Tab A");
      
      clickAndWait("link=Tab B");
      assertTextPresent("Content of Tab B");
      
      clickAndWait("link=Tab C");
      assertTextPresent("Content of Tab C");
      assertTextPresent("Content of Tab X");
      
      //Inner tabs
      clickAndWait("link=Tab Y");
      assertTextPresent("Content of Tab C");
      assertTextPresent("Content of Tab Y");
      
      clickAndWait("link=Tab A");
      assertTextPresent("Content of Tab A");
   }
   
   @Test
   public void check_if_tab_panel_changes_content_on_clicking_title_links_for_ajax() throws InterruptedException
   {
      openBaseURL();
      clickAndWait("link=Ajax TabPanel Demo");
      
      //By default first tab should be displayed
      assertTextPresent("Content of Tab A");
      
      click("link=Tab B");
      Thread.sleep(1500);
      assertTextPresent("Content of Tab B");
      
      click("link=Tab C");
      Thread.sleep(1500);
      assertTextPresent("Content of Tab C");
      assertTextPresent("Content of Tab X");
      
      //Inner tabs
      click("link=Tab Y");
      Thread.sleep(1500);
      assertTextPresent("Content of Tab C");
      assertTextPresent("Content of Tab Y");
      
      click("link=Tab A");
      Thread.sleep(1500);
      assertTextPresent("Content of Tab A");
   }

}
