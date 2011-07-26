package com.googlecode.tawus.ajaxupload.integration.pages;

import java.util.List;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.upload.services.UploadedFile;

import com.googlecode.tawus.ajaxupload.integration.TestUtils;

public class SingleAjaxUpload
{
   @Persist
   @Property
   private List<UploadedFile> uploads;
   
   @Property
   @Persist
   private String textValue;

   @SuppressWarnings("unused")
   @Property
   @Persist(PersistenceConstants.FLASH)
   private String fileContent;

   @SuppressWarnings("unused")
   @Property
   @Persist(PersistenceConstants.FLASH)
   private String message;
   
   @InjectComponent
   private Form form;
   
   private String context;
   
   void onActivate()
   {
   }
   
   void onActivate(String context)
   {
      this.context = context;
   }
   
   String onPassivate()
   {
      return context;
   }
   
   void onValidateFromForm()
   {
      if(textValue.equals("Fail"))
      {
         form.recordError("Failure");
      }
   }

   void onSuccessFromForm()
   {
      if (uploads == null || uploads.size() == 0)
      {
         message = "Nothing to upload";
      }
      else
      {
         message = "File uploaded";
         StringBuilder sb = new StringBuilder();
         for(UploadedFile upload: uploads)
         {
            sb.append(TestUtils.convertStreamToString(upload.getStream()).toString());
         }
         
         fileContent = sb.toString();
         uploads = null;
      }

   }
}
