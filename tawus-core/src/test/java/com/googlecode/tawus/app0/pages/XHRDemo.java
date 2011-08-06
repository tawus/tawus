package com.googlecode.tawus.app0.pages;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;

import com.googlecode.tawus.annotations.XHR;

public class XHRDemo
{
   @InjectComponent
   private Zone zone;
   
   @SuppressWarnings("unused")
   @Property
   @Persist(PersistenceConstants.FLASH)
   private String message;
   
   @XHR
   public Object onActionFromLink()
   {
      message = "Clicked";
      return zone.getBody();
   }

}
