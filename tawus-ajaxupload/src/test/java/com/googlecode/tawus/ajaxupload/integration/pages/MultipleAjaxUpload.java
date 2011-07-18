package com.googlecode.tawus.ajaxupload.integration.pages;

import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ApplicationGlobals;
import org.apache.tapestry5.upload.services.UploadedFile;

import com.googlecode.tawus.ajaxupload.components.AjaxUpload;

public class MultipleAjaxUpload
{
   @SuppressWarnings("unused")
   @Inject
   private ApplicationGlobals globals;

   @InjectComponent
   private Form uploadForm;
   @Persist
   @Property
   private List<UploadedFile> uploads;

   @SuppressWarnings("unused")
   @Component(parameters = { "maxFiles=2" })
   private AjaxUpload ajaxUpload;

   @SetupRender
   void setupRender()
   {
   }

   public Object onSuccess()
   {
      // Use uploads
      for(int i = 0; i < uploads.size(); i++)
      {
         System.out.println("File Name: " + uploads.get(i).getFileName());
         System.out.println("File size: " + uploads.get(i).getSize());

      }

      System.out.println("File Number: " + uploads.size());

      if(uploads.size() < 2)
         uploadForm.recordError("You must upload two files!!");

      uploads.clear();

      return this;
   }

}
