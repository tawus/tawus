package com.googlecode.tawus.ajaxupload.internal;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry5.services.HttpServletRequestFilter;
import org.apache.tapestry5.services.HttpServletRequestHandler;

import com.googlecode.tawus.ajaxupload.services.AjaxUploadDecoder;

public class AjaxUploadServletRequestFilter implements HttpServletRequestFilter
{

   private AjaxUploadDecoder decoder;

   public AjaxUploadServletRequestFilter(AjaxUploadDecoder decoder)
   {
      this.decoder = decoder;
   }

   @Override
   public boolean service(HttpServletRequest request, HttpServletResponse response, HttpServletRequestHandler handler)
         throws IOException
   {
      if (decoder.isAjaxUploadRequest(request))
      {
         decoder.setupUploadedFile(request);
      }

      return handler.service(request, response);
   }

}
