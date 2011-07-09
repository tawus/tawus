package com.googlecode.tawus.addons.integration.pages;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.googlecode.tawus.addons.ModalDialogUtils;

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
   
   @InjectComponent
   private Form form;

   private boolean hide;
   
   void beginRender()
   {
     
   }
   
   Object onSuccess()
   {
      message = String.format("Hello %s, your address is %s", name, address);
      if(hide)
      {
         return ModalDialogUtils.createJSONToCloseDialog();
      }
       return zone.getBody();
   }
   
   Object onFailure()
   {
      message = String.format("Submission failed");
      return zone.getBody();
   }
   
   void onSelectedFromSubmitAndHide()
   {
      hide = true;
   }
   
   Block onShowDialog()
   {
      form.clearErrors();
      return formBlock;
   }
}
