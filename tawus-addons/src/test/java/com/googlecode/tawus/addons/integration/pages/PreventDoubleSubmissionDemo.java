package com.googlecode.tawus.addons.integration.pages;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

public class PreventDoubleSubmissionDemo
{
   @Persist
   @Property
   private int counter;
   
   void onSubmit() throws InterruptedException
   {
      counter++;
      Thread.sleep(300);
   }
   
   void onReset()
   {
      counter = 0;
   }
   
}
