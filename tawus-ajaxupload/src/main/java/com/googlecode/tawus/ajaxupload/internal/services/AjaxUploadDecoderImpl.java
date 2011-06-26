package com.googlecode.tawus.ajaxupload.internal.services;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.upload.internal.services.UploadedFileItem;
import org.apache.tapestry5.upload.services.UploadedFile;

import com.googlecode.tawus.ajaxupload.AjaxUploadConstants;
import com.googlecode.tawus.ajaxupload.services.AjaxUploadDecoder;

public class AjaxUploadDecoderImpl implements AjaxUploadDecoder
{
   private UploadedFileItem uploadedFile;
   
   public static final String AJAX_UPLOAD_HEADER = "X-File-Name"; 

   private FileItemFactory fileItemFactory;

   public AjaxUploadDecoderImpl(FileItemFactory fileItemFactory)
   {
      this.fileItemFactory = fileItemFactory;
   }

   @Override
   public boolean isAjaxUploadRequest(HttpServletRequest request)
   {
      return request.getHeader(AJAX_UPLOAD_HEADER) != null;
   }
   
   @Override
   public boolean isAjaxUploadRequest(Request request)
   {
      return request.isXHR() && request.getHeader(AJAX_UPLOAD_HEADER) != null;
   }

   @Override
   public void setupUploadedFile(HttpServletRequest request)
   {
      String fieldName = request.getHeader(AJAX_UPLOAD_HEADER);
      FileItem item = fileItemFactory.createItem(fieldName, request.getContentType(), false,
            request.getParameter(AjaxUploadConstants.FILE_PARAMETER));
      try
      {
         TapestryInternalUtils.copy(request.getInputStream(), item.getOutputStream());
      }
      catch (IOException e)
      {
         throw new RuntimeException("Could not copy request's input stream to file", e);
      }

      uploadedFile = new UploadedFileItem(item);
   }

   @Override
   public UploadedFile getFileUpload()
   {
      return uploadedFile;
   }

}
