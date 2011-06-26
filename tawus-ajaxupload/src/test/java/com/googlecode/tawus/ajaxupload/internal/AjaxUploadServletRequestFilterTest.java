package com.googlecode.tawus.ajaxupload.internal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry5.services.HttpServletRequestHandler;
import org.apache.tapestry5.test.TapestryTestCase;
import org.testng.annotations.Test;

import com.googlecode.tawus.ajaxupload.internal.AjaxUploadServletRequestFilter;
import com.googlecode.tawus.ajaxupload.services.AjaxUploadDecoder;

public class AjaxUploadServletRequestFilterTest extends TapestryTestCase
{
   @Test
   public void check_non_ajax_upload_request_does_not_setup_ajax_upload_decoder() throws Exception
   {
      AjaxUploadDecoder decoder = mockAjaxUploadDecoder();
      
      AjaxUploadServletRequestFilter filter = new AjaxUploadServletRequestFilter(decoder);
      
      HttpServletRequest request = mockHttpServletRequest();
      HttpServletResponse response = mockHttpServletResponse();
      HttpServletRequestHandler handler = mockHttpServletRequestHandler();
      
      expect(handler.service(request, response)).andReturn(true);
      expect(decoder.isAjaxUploadRequest(request)).andReturn(false);
      
      replay();
      
      filter.service(request, response, handler);
      
      verify();
   }
   
   @Test
   public void check_ajax_upload_request_does_setup_ajax_upload_decoder() throws Exception
   {
      AjaxUploadDecoder decoder = mockAjaxUploadDecoder();
      
      AjaxUploadServletRequestFilter filter = new AjaxUploadServletRequestFilter(decoder);
      
      HttpServletRequest request = mockHttpServletRequest();
      HttpServletResponse response = mockHttpServletResponse();
      HttpServletRequestHandler handler = mockHttpServletRequestHandler();
      
      expect(handler.service(request, response)).andReturn(true);
      expect(decoder.isAjaxUploadRequest(request)).andReturn(true);
      decoder.setupUploadedFile(request);
      
      replay();
      
      filter.service(request, response, handler);
      
      verify();
   }
   

   private AjaxUploadDecoder mockAjaxUploadDecoder()
   {
      return newMock(AjaxUploadDecoder.class);
   }

}
