package com.googlecode.tawus.addons.integration.pages;

import org.apache.tapestry5.annotations.Property;

public class TabPanelDemo
{
   @Property
   private String active;
  
   void onActivate(String active)
   {
      this.active = active;
   }
   
   String onPassivate()
   {
      return active;
   }
   
}
