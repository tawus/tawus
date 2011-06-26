package com.googlecode.tawus.ajaxupload.internal.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.test.TapestryTestCase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.googlecode.tawus.ajaxupload.AjaxUploadConstants;
import com.googlecode.tawus.ajaxupload.services.AjaxUploadDecoder;

public class AjaxUploadDecoderImplTest extends TapestryTestCase
{
   private AjaxUploadDecoder decoder;
   private FileItemFactory fileItemFactory;
   
   
   @BeforeMethod
   public void setup()
   {
      fileItemFactory = mockFileItemFactory();
      decoder = new AjaxUploadDecoderImpl(fileItemFactory);
   }
   
   @Test
   public void check_ajax_upload_request_calls_decoder()
   {
      Request request = mockRequest();
      
      expect(request.getHeader(AjaxUploadDecoderImpl.AJAX_UPLOAD_HEADER)).andReturn("test");
      expect(request.isXHR()).andReturn(true);
      
      replay();
      
      assertTrue(decoder.isAjaxUploadRequest(request));
      
      verify();
      
   }
   
   @Test
   public void check_ajax_upload_request_calls_decoder_with_http_servlet_request()
   {
      HttpServletRequest request = mockHttpServletRequest();
      
      expect(request.getHeader(AjaxUploadDecoderImpl.AJAX_UPLOAD_HEADER)).andReturn("test");
      
      replay();
      
      assertTrue(decoder.isAjaxUploadRequest(request));
      
      verify();
      
   }
   
   @Test
   public void check_ajax_request_does_not_call_decoder_if_header_not_present()
   {
      Request request = mockRequest();
      
      expect(request.isXHR()).andReturn(true);
      expect(request.getHeader(AjaxUploadDecoderImpl.AJAX_UPLOAD_HEADER)).andReturn(null);
      
      replay();
      
      assertFalse(decoder.isAjaxUploadRequest(request));
      
      verify();
   }
   
   @Test
   public void check_non_ajax_request_does_not_call_decoder_if_header_not_present()
   {
      Request request = mockRequest();
      
      expect(request.isXHR()).andReturn(false);
      
      replay();
      
      assertFalse(decoder.isAjaxUploadRequest(request));
      
      verify();
   }
   
   @Test
   public void check_request_does_not_call_decoder_if_header_not_present_with_http_servlet_request()
   {
      HttpServletRequest request = mockHttpServletRequest();
      
      expect(request.getHeader(AjaxUploadDecoderImpl.AJAX_UPLOAD_HEADER)).andReturn(null);
      
      replay();
      
      assertFalse(decoder.isAjaxUploadRequest(request));
      
      verify();
      
   }
   
   @Test
   public void check_new_uploaded_file_is_created_during_setup() throws Exception
   {
      HttpServletRequest request = mockHttpServletRequest();
      FileItem item = mockFileItem();
      ServletInputStream inputStream = newStringInputStream("Test stream data");
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      
      expect(request.getHeader(AjaxUploadDecoderImpl.AJAX_UPLOAD_HEADER)).andReturn("test");
      expect(request.getContentType()).andReturn("test/test");
      expect(request.getParameter(AjaxUploadConstants.FILE_PARAMETER)).andReturn("myfilename");
      expect(request.getInputStream()).andReturn(inputStream);
      
      expect(fileItemFactory.createItem("test", "test/test", false, "myfilename")).andReturn(item);
      expect(item.getOutputStream()).andReturn(outputStream);
      
      replay();
      
      decoder.setupUploadedFile(request);
      
      verify();
      
      assertEquals(outputStream.toString(), "Test stream data");
   }

   private FileItemFactory mockFileItemFactory()
   {
      return newMock(FileItemFactory.class);
   }
   
   private FileItem mockFileItem()
   {
      return newMock(FileItem.class);
   }

   private ServletInputStream newStringInputStream(String text)
   {
      final ByteArrayInputStream in = new ByteArrayInputStream(text.getBytes());
      return new ServletInputStream(){

         @Override
         public int read() throws IOException
         {
            return in.read();
         }
         
      };
   }

}
