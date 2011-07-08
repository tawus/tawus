package com.googlecode.tawus.addons.integration.pages;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;

public class ModalDialogWithForm
{
   @Property
   @Persist(PersistenceConstants.FLASH)
   private String name;
   
   @Property
   @Persist(PersistenceConstants.FLASH)
   private String address;

   @Inject
   private Block formBlock;

   @InjectComponent
   private Zone zone;
   
   @SuppressWarnings("unused")
   @Property
   private String message;
   
   void onSuccess()
   {
      message = String.format("Hello %s, your address is %s", name, address);
   }
   
   void onFailure()
   {
      message = String.format("Submission failed");
   }
   
   Object onSubmit()
   {
      return zone;
   }
   
   Block onShowDialog()
   {
      return formBlock;
   }
}
