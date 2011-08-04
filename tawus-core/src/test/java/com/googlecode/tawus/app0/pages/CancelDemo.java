package com.googlecode.tawus.app0.pages;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;

/**
 * A page to demonstrate the use of {@link com.googlecode.tawus.components.Cancel cancel-button} for 
 * a form.
 */
public class CancelDemo
{
   @SuppressWarnings("unused")
   @Property
   @Persist(PersistenceConstants.FLASH)
   private String message;
   
   @InjectComponent
   private Zone zone;

   void onCancelFromCancelForm()
   {
      message = "Form Cancelled";
   }
   
   Object onCancelFromCancelAjaxForm()
   {
      message = "Ajax Form Cancelled";
      return zone.getBody();
   }

}
